# 分析源码之-------LinkedBlockingQueue.java
>  介绍
 
    /**
     * A variant of the "two lock queue" algorithm.  The putLock gates
        两种锁队列算法
     * entry to put (and offer), and has an associated condition for
        放锁的入口，关联一个等待放的条件
     * waiting puts.  Similarly for the takeLock.  The "count" field
                        拿锁也是同样的。
     * that they both rely on is maintained as an atomic to avoid
        放锁与拿锁都依赖count属性，为了防止大多数情况下同时获取这两种锁，
     * needing to get both locks in most cases. Also, to minimize need
            该属性由一个原子变量维护
     * for puts to get takeLock and vice-versa, cascading notifies are
      为了最小化获取拿锁的需要,放锁亦然              采用相应的通知
     * used. When a put notices that it has enabled at least one take,
                当放一个元素时 通知 它已经可以至少被一次拿取
     * it signals taker. That taker in turn signals others if more
       它就发信号给拿取者
     * items have been entered since the signal. And symmetrically for

     * takes signalling puts. Operations such as remove(Object) and
     * iterators acquire both locks.
     *
     * Visibility between writers and readers is provided as follows:
     *
     * Whenever an element is enqueued, the putLock is acquired and
     当一个元素加入队列时，要获取放锁并且更新count属性。
     * count updated.  A subsequent reader guarantees visibility to the

     * enqueued Node by either acquiring the putLock (via fullyLock)

     * or by acquiring the takeLock, and then reading n = count.get();

     * this gives visibility to the first n items.
     *
     * To implement weakly consistent iterators, it appears we need to

     * keep all Nodes GC-reachable from a predecessor dequeued Node.

     * That would cause two problems:
     * - allow a rogue Iterator to cause unbounded memory retention
     * - cause cross-generational linking of old Nodes to new Nodes if
     *   a Node was tenured while live, which generational GCs have a
     *   hard time dealing with, causing repeated major collections.
     * However, only non-deleted Nodes need to be reachable from
     * dequeued Nodes, and reachability does not necessarily have to
     * be of the kind understood by the GC.  We use the trick of
     * linking a Node that has just been dequeued to itself.  Such a
     * self-link implicitly means to advance to head.next.
     */
#`静态内部类Node<E>` 链表
    /**
     * Linked list node class  //静态类 节点Node，链表节点
     */
    static class Node<E> {
        E item;

        /**
         * One of:
         * - the real successor Node
         * - this Node, meaning the successor is head.next
         * - null, meaning there is no successor (this is the last node)
         */
        Node<E> next;

        Node(E x) { item = x; }
    }

# 属性
`capacity`

    /** The capacity bound, or Integer.MAX_VALUE if none  容量边界*/
   private final int capacity;
`元素个数，AtomicInteger`

    /** Current number of elements  元素个数，原子变量*/
    private final AtomicInteger count = new AtomicInteger();


`head` 

    /**
     * Head of linked list. 链表的头
     * Invariant: head.item == null  头结点永远为null
     */
    transient Node<E> head;
`last`

    /**
     * Tail of linked list.链表的尾
     * Invariant: last.next == null  尾节点的next节点永远为null
     */
    private transient Node<E> last;


`takeLock 拿锁`

    /** Lock held by take, poll, etc 拿锁*/
    private final ReentrantLock takeLock = new ReentrantLock();
`notEmpty 非空条件`

    /** Wait queue for waiting takes 等待队列等待拿*/
    private final Condition notEmpty = takeLock.newCondition();
`putLock 放锁`

    /** Lock held by put, offer, etc 放锁*/
    private final ReentrantLock putLock = new ReentrantLock();
`notFull 条件`

    /** Wait queue for waiting puts 等待队列等待放*/
    private final Condition notFull = putLock.newCondition();

`signalNotEmpty() 发送非空信号`

    /**
     * Signals a waiting take. Called only from put/offer (which do not
     * otherwise ordinarily lock takeLock.)
     */
    private void signalNotEmpty() {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();//1.先获取拿锁，获取锁后，禁止其他线程拿取，能保证当前队列不空的状态，而后进行第二步
        try {
            notEmpty.signal();//2.通知 等待条件上等待"拿"的线程
        } finally {
            takeLock.unlock();
        }
    }

`signalNotFull() 发送不满信号`

       /**
        * Signals a waiting put. Called only from take/poll.//通知 等待放的线程
        */
       private void signalNotFull() {
           final ReentrantLock putLock = this.putLock;
           putLock.lock();// 1. 先获取放锁，获取锁后，保持队列notfull状态，而后进行第二步的通知 "可以放了"
           try {
               notFull.signal();//2. 通知 在等待条件上等待"放"的线程
           } finally {
               putLock.unlock();
           }
       }


## enqueue(Node<E> node) 队列尾部链接添加
    /**
     * Links node at end of queue.  
     *
     * @param node the node
     */
    private void enqueue(Node<E> node) {
        // assert putLock.isHeldByCurrentThread();
        // assert last.next == null;
        last = last.next = node;
    }
    
## private E dequeue()     移除头结点
        /**
         * Removes a node from head of queue.
         *
         * @return the node
         */
        private E dequeue() {
            // assert takeLock.isHeldByCurrentThread();
            // assert head.item == null;
            Node<E> h = head;
            Node<E> first = h.next; // 第一个节点是谁？？？
            h.next = h; // help GC  //? why help GC，   <font color=#0099ff>是指队列的头结点指向自己</font>
            head = first; //头结点指向第一个节点,表示 first节点自带 next引用，故head就自带next引用
            E x = first.item;
            first.item = null; // 头结点 item 恒为空
            return x;
        }
##  void fullyLock() 防止读写  
        /**
         * Locks to prevent both puts and takes.
         */
        void fullyLock() {
            putLock.lock();
            takeLock.lock();
        }
##  void fullyUnlock()  
        /**
         * Unlocks to allow both puts and takes.
         */
        void fullyUnlock() {
            takeLock.unlock();
            putLock.unlock();
        }
    
    //     /**
    //      * Tells whether both locks are held by current thread.
    //      */
    //     boolean isFullyLocked() {
    //         return (putLock.isHeldByCurrentThread() &&
    //                 takeLock.isHeldByCurrentThread());
    //     }
##  构造 public LinkedBlockingQueue()，大小为Integer.MAX_VALUE
        /**
         * Creates a {@code LinkedBlockingQueue} with a capacity of
         * {@link Integer#MAX_VALUE}.
         */
        public LinkedBlockingQueue() {
            this(Integer.MAX_VALUE);
        }
##  构造 public LinkedBlockingQueue(int capacity)   
        /**
         * Creates a {@code LinkedBlockingQueue} with the given (fixed) capacity.
         *
         * @param capacity the capacity of this queue
         * @throws IllegalArgumentException if {@code capacity} is not greater
         *         than zero
         */
        public LinkedBlockingQueue(int capacity) {
            if (capacity <= 0) throw new IllegalArgumentException();
            this.capacity = capacity;
            last = head = new Node<E>(null);
        }
##  构造 public LinkedBlockingQueue(Collection<? extends E> c)
> 采用Collection中元素填充队列
> 填充顺序依赖集合iterator

        /**
         * Creates a {@code LinkedBlockingQueue} with a capacity of
         * {@link Integer#MAX_VALUE}, initially containing the elements of the
         * given collection,
         * added in traversal order of the collection's iterator.
         *
         * @param c the collection of elements to initially contain
         * @throws NullPointerException if the specified collection or any
         *         of its elements are null
         */
        public LinkedBlockingQueue(Collection<? extends E> c) {
            this(Integer.MAX_VALUE);
            final ReentrantLock putLock = this.putLock;
            putLock.lock(); // Never contended, but necessary for visibility // 这里构造单线程
            try {
                int n = 0;
                for (E e : c) {
                    if (e == null)
                        throw new NullPointerException();
                    if (n == capacity)
                        throw new IllegalStateException("Queue full");
                    enqueue(new Node<E>(e)); //尾节点添加
                    ++n;//单线程安全
                }
                count.set(n);// 元素个数
            } finally {
                putLock.unlock();
            }
        }
##    int size() 返回原子变量的大小
        // this doc comment is overridden to remove the reference to collections
        // greater in size than Integer.MAX_VALUE
        /**
         * Returns the number of elements in this queue.
         *
         * @return the number of elements in this queue
         */
        public int size() {
            return count.get();
        }
## _int remainingCapacity_()
#### 剩余容量    
        // this doc comment is a modified copy of the inherited doc comment,
        // without the reference to unlimited queues.
        /**
         * Returns the number of additional elements that this queue can ideally
         * (in the absence of memory or resource constraints) accept without
         * blocking. This is always equal to the initial capacity of this queue
         * less the current {@code size} of this queue.
         *
         * <p>Note that you <em>cannot</em> always tell if an attempt to insert
         * an element will succeed by inspecting {@code remainingCapacity}
         * because it may be the case that another thread is about to
         * insert or remove an element.
         */
        public int remainingCapacity() {
            return capacity - count.get();
        }
##  _void put(E e)_ 放元素   
        /**
         * Inserts the specified element at the tail of this queue, waiting if
         * necessary for space to become available.
         *
         * @throws InterruptedException {@inheritDoc}
         * @throws NullPointerException {@inheritDoc}
         */
        public void put(E e) throws InterruptedException {
            if (e == null) throw new NullPointerException();
            // Note: convention in all put/take/etc is to preset local var
            // holding count negative to indicate failure unless set.
            int c = -1;
            Node<E> node = new Node<E>(e);
            final ReentrantLock putLock = this.putLock; 
            final AtomicInteger count = this.count;
            putLock.lockInterruptibly();//1.获取锁
            try {
                /*
                 * Note that count is used in wait guard even though it is
                 * not protected by lock. This works because count can
                 * only decrease at this point (all other puts are shut
                 * out by lock), and we (or some other waiting put) are
                 * signalled if it ever changes from capacity. Similarly
                 * for all other uses of count in other wait guards.
                 */
                while (count.get() == capacity) {//2.元素个数 与 容量比较，如果满，等待
                    notFull.await();//2.1条件等待，被唤醒后，仍然进入while进行条件判断
                }
                enqueue(node);//3.放入队列尾部
                c = count.getAndIncrement();//4.增加原子个数
                if (c + 1 < capacity)//5.如果还能放一个元素，就通知notFull条件上等待的线程
                    notFull.signal();
            } finally {
                putLock.unlock();
            }
            if (c == 0)//6.为何用0，不知道，c值在原来-1的基础上增加为0,说明有元素放入，至少不为空
                signalNotEmpty();//6.1  通知消费，在notEmpty条件上等待的元素
        }
## _boolean offer(E e, long timeout, TimeUnit unit)_   
>  _加入队列尾，等待指定时间，直到有空间_

        /**
         * Inserts the specified element at the tail of this queue, waiting if
         * necessary up to the specified wait time for space to become available.
         *
         * @return {@code true} if successful, or {@code false} if
         *         the specified waiting time elapses before space is available
         * @throws InterruptedException {@inheritDoc}
         * @throws NullPointerException {@inheritDoc}
         */
        public boolean offer(E e, long timeout, TimeUnit unit)
            throws InterruptedException {
    
            if (e == null) throw new NullPointerException();
            long nanos = unit.toNanos(timeout);
            int c = -1;
            final ReentrantLock putLock = this.putLock;
            final AtomicInteger count = this.count;
            putLock.lockInterruptibly();
            try {
                while (count.get() == capacity) {
                    if (nanos <= 0)// 没有等待时间，当前判断满时直接返回false
                        return false;
                    nanos = notFull.awaitNanos(nanos);
                }
                enqueue(new Node<E>(e));
                c = count.getAndIncrement();
                if (c + 1 < capacity)
                    notFull.signal();
            } finally {
                putLock.unlock();
            }
            if (c == 0)
                signalNotEmpty();
            return true;
        }
##  boolean offer(E e)
> 队列尾部增加节点

        /**
         * Inserts the specified element at the tail of this queue if it is
         * possible to do so immediately without exceeding the queue's capacity,
         * returning {@code true} upon success and {@code false} if this queue
         * is full.
         * When using a capacity-restricted queue, this method is generally
         * preferable to method {@link BlockingQueue#add add}, which can fail to
         * insert an element only by throwing an exception.
         *
         * @throws NullPointerException if the specified element is null
         */
        public boolean offer(E e) {
            if (e == null) throw new NullPointerException();
            final AtomicInteger count = this.count;
            if (count.get() == capacity)
                return false;
            int c = -1;
            Node<E> node = new Node<E>(e);
            final ReentrantLock putLock = this.putLock;
            putLock.lock();
            try {
                if (count.get() < capacity) {
                    enqueue(node);
                    c = count.getAndIncrement();
                    if (c + 1 < capacity)
                        notFull.signal();
                }
            } finally {
                putLock.unlock();
            }
            if (c == 0)
                signalNotEmpty();
            return c >= 0;
        }
##   E take() 
> 内部实现：dequeue()

        public E take() throws InterruptedException {
            E x;
            int c = -1;
            final AtomicInteger count = this.count;
            final ReentrantLock takeLock = this.takeLock;
            takeLock.lockInterruptibly();
            try {
                while (count.get() == 0) { //队列为空，等待
                    notEmpty.await();
                }
                x = dequeue();
                c = count.getAndDecrement(); //减法
                if (c > 1)
                    notEmpty.signal();
            } finally {
                takeLock.unlock();
            }
            if (c == capacity)
                signalNotFull();
            return x;
        }
##  E poll(long timeout, TimeUnit unit)
> 带有超时版本的 take()

        public E poll(long timeout, TimeUnit unit) throws InterruptedException {
            E x = null;
            int c = -1;
            long nanos = unit.toNanos(timeout);
            final AtomicInteger count = this.count;
            final ReentrantLock takeLock = this.takeLock;
            takeLock.lockInterruptibly();
            try {
                while (count.get() == 0) {
                    if (nanos <= 0)
                        return null;
                    nanos = notEmpty.awaitNanos(nanos);
                }
                x = dequeue();
                c = count.getAndDecrement();
                if (c > 1)
                    notEmpty.signal();
            } finally {
                takeLock.unlock();
            }
            if (c == capacity)
                signalNotFull();
            return x;
        }
##  E poll()  
        public E poll() {
            final AtomicInteger count = this.count;
            if (count.get() == 0)
                return null;
            E x = null;
            int c = -1;
            final ReentrantLock takeLock = this.takeLock;
            takeLock.lock();
            try {
                if (count.get() > 0) {
                    x = dequeue();
                    c = count.getAndDecrement();
                    if (c > 1)
                        notEmpty.signal();
                }
            } finally {
                takeLock.unlock();
            }
            if (c == capacity)
                signalNotFull();
            return x;
        }
##  E peek(),返回头结点
        public E peek() {
            if (count.get() == 0)
                return null;
            final ReentrantLock takeLock = this.takeLock;
            takeLock.lock();
            try {
                Node<E> first = head.next;
                if (first == null)
                    return null;
                else
                    return first.item;
            } finally {
                takeLock.unlock();
            }
        }
    
        /**
         * Unlinks interior Node p with predecessor trail.
         */
        void unlink(Node<E> p, Node<E> trail) {
            // assert isFullyLocked();
            // p.next is not changed, to allow iterators that are
            // traversing p to maintain their weak-consistency guarantee.
            p.item = null;
            trail.next = p.next;
            if (last == p)
                last = trail;
            if (count.getAndDecrement() == capacity)
                notFull.signal();
        }
##  boolean remove(Object o)
> 从队列删除节点

        /**
         * Removes a single instance of the specified element from this queue,
         * if it is present.  More formally, removes an element {@code e} such
         * that {@code o.equals(e)}, if this queue contains one or more such
         * elements.
         * Returns {@code true} if this queue contained the specified element
         * (or equivalently, if this queue changed as a result of the call).
         *
         * @param o element to be removed from this queue, if present
         * @return {@code true} if this queue changed as a result of the call
         */
        public boolean remove(Object o) {
            if (o == null) return false;
            fullyLock();//获取 放锁和拿锁 两个锁
            try {
                for (Node<E> trail = head, p = trail.next;
                     p != null;
                     trail = p, p = p.next) {
                    if (o.equals(p.item)) {
                        unlink(p, trail);
                        return true;
                    }
                }
                return false;
            } finally {
                fullyUnlock();
            }
        }
##  boolean contains(Object o)  
        /**
         * Returns {@code true} if this queue contains the specified element.
         * More formally, returns {@code true} if and only if this queue contains
         * at least one element {@code e} such that {@code o.equals(e)}.
         *
         * @param o object to be checked for containment in this queue
         * @return {@code true} if this queue contains the specified element
         */
        public boolean contains(Object o) {
            if (o == null) return false;
            fullyLock();
            try {
                for (Node<E> p = head.next; p != null; p = p.next)// 链表的遍历
                    if (o.equals(p.item))
                        return true;
                return false;
            } finally {
                fullyUnlock();
            }
        }
## Object[] toArray()  
> 链表遍历

        /**
         * Returns an array containing all of the elements in this queue, in
         * proper sequence.
         *
         * <p>The returned array will be "safe" in that no references to it are
         * maintained by this queue.  (In other words, this method must allocate
         * a new array).  The caller is thus free to modify the returned array.
         *
         * <p>This method acts as bridge between array-based and collection-based
         * APIs.
         *
         * @return an array containing all of the elements in this queue
         */
        public Object[] toArray() {
            fullyLock();
            try {
                int size = count.get();
                Object[] a = new Object[size];
                int k = 0;
                for (Node<E> p = head.next; p != null; p = p.next)// 链表的遍历，从first节点开始
                    a[k++] = p.item;
                return a;
            } finally {
                fullyUnlock();
            }
        }
## <T> T[] toArray(T[] a)
> 遍历链表，放入指定的数组

        /**
         * Returns an array containing all of the elements in this queue, in
         * proper sequence; the runtime type of the returned array is that of
         * the specified array.  If the queue fits in the specified array, it
         * is returned therein.  Otherwise, a new array is allocated with the
         * runtime type of the specified array and the size of this queue.
         *
         * <p>If this queue fits in the specified array with room to spare
         * (i.e., the array has more elements than this queue), the element in
         * the array immediately following the end of the queue is set to
         * {@code null}.
         *
         * <p>Like the {@link #toArray()} method, this method acts as bridge between
         * array-based and collection-based APIs.  Further, this method allows
         * precise control over the runtime type of the output array, and may,
         * under certain circumstances, be used to save allocation costs.
         *
         * <p>Suppose {@code x} is a queue known to contain only strings.
         * The following code can be used to dump the queue into a newly
         * allocated array of {@code String}:
         *
         *  <pre> {@code String[] y = x.toArray(new String[0]);}</pre>
         *
         * Note that {@code toArray(new Object[0])} is identical in function to
         * {@code toArray()}.
         *
         * @param a the array into which the elements of the queue are to
         *          be stored, if it is big enough; otherwise, a new array of the
         *          same runtime type is allocated for this purpose
         * @return an array containing all of the elements in this queue
         * @throws ArrayStoreException if the runtime type of the specified array
         *         is not a supertype of the runtime type of every element in
         *         this queue
         * @throws NullPointerException if the specified array is null
         */
        @SuppressWarnings("unchecked")
        public <T> T[] toArray(T[] a) {
            fullyLock();// 锁.....
            try {
                int size = count.get();
                if (a.length < size)//如果数组长度不够，创建新的数组，大小为队列元素
                    a = (T[])java.lang.reflect.Array.newInstance
                        (a.getClass().getComponentType(), size);
    
                int k = 0;
                for (Node<E> p = head.next; p != null; p = p.next)
                    a[k++] = (T)p.item;
                if (a.length > k)
                    a[k] = null;
                return a;
            } finally {
                fullyUnlock();
            }
        }
## String toString()
> 采用for(;;)循环

        public String toString() {
            fullyLock();
            try {
                Node<E> p = head.next;
                if (p == null)
                    return "[]";
    
                StringBuilder sb = new StringBuilder();
                sb.append('[');
                for (;;) {
                    E e = p.item;
                    sb.append(e == this ? "(this Collection)" : e);
                    p = p.next;
                    if (p == null)//跳出条件
                        return sb.append(']').toString();
                    sb.append(',').append(' ');
                }
            } finally {
                fullyUnlock();
            }
        }
## void clear()
> 遍历中元素置空，个数置零

        /**
         * Atomically removes all of the elements from this queue.
         * The queue will be empty after this call returns.
         */
        public void clear() {
            fullyLock();
            try {
                for (Node<E> p, h = head; (p = h.next) != null; h = p) {//遍历置空
                    h.next = h;
                    p.item = null;//置空
                }
                head = last;
                // assert head.item == null && head.next == null;
                if (count.getAndSet(0) == capacity)
                    notFull.signal();
            } finally {
                fullyUnlock();
            }
        }
    
        /**
         * @throws UnsupportedOperationException {@inheritDoc}
         * @throws ClassCastException            {@inheritDoc}
         * @throws NullPointerException          {@inheritDoc}
         * @throws IllegalArgumentException      {@inheritDoc}
         */
        public int drainTo(Collection<? super E> c) {
            return drainTo(c, Integer.MAX_VALUE);
        }
    
        /**
         * @throws UnsupportedOperationException {@inheritDoc}
         * @throws ClassCastException            {@inheritDoc}
         * @throws NullPointerException          {@inheritDoc}
         * @throws IllegalArgumentException      {@inheritDoc}
         */
        public int drainTo(Collection<? super E> c, int maxElements) {
            if (c == null)
                throw new NullPointerException();
            if (c == this)
                throw new IllegalArgumentException();
            if (maxElements <= 0)
                return 0;
            boolean signalNotFull = false;
            final ReentrantLock takeLock = this.takeLock;
            takeLock.lock();
            try {
                int n = Math.min(maxElements, count.get());
                // count.get provides visibility to first n Nodes
                Node<E> h = head;
                int i = 0;
                try {
                    while (i < n) {
                        Node<E> p = h.next;
                        c.add(p.item);
                        p.item = null;
                        h.next = h;
                        h = p;
                        ++i;
                    }
                    return n;
                } finally {
                    // Restore invariants even if c.add() threw
                    if (i > 0) {
                        // assert h.item == null;
                        head = h;
                        signalNotFull = (count.getAndAdd(-i) == capacity);
                    }
                }
            } finally {
                takeLock.unlock();
                if (signalNotFull)
                    signalNotFull();
            }
        }
    
        /**
         * Returns an iterator over the elements in this queue in proper sequence.
         * The elements will be returned in order from first (head) to last (tail).
         *
         * <p>The returned iterator is
         * <a href="package-summary.html#Weakly"><i>weakly consistent</i></a>.
         *
         * @return an iterator over the elements in this queue in proper sequence
         */
        public Iterator<E> iterator() {
            return new Itr();
        }
    
        private class Itr implements Iterator<E> {
            /*
             * Basic weakly-consistent iterator.  At all times hold the next
             * item to hand out so that if hasNext() reports true, we will
             * still have it to return even if lost race with a take etc.
             */
    
            private Node<E> current;
            private Node<E> lastRet;
            private E currentElement;
    
            Itr() {
                fullyLock();
                try {
                    current = head.next;
                    if (current != null)
                        currentElement = current.item;
                } finally {
                    fullyUnlock();
                }
            }
    
            public boolean hasNext() {
                return current != null;
            }
    
            /**
             * Returns the next live successor of p, or null if no such.
             *
             * Unlike other traversal methods, iterators need to handle both:
             * - dequeued nodes (p.next == p)
             * - (possibly multiple) interior removed nodes (p.item == null)
             */
            private Node<E> nextNode(Node<E> p) {
                for (;;) {
                    Node<E> s = p.next;
                    if (s == p)
                        return head.next;
                    if (s == null || s.item != null)
                        return s;
                    p = s;
                }
            }
    
            public E next() {
                fullyLock();
                try {
                    if (current == null)
                        throw new NoSuchElementException();
                    E x = currentElement;
                    lastRet = current;
                    current = nextNode(current);
                    currentElement = (current == null) ? null : current.item;
                    return x;
                } finally {
                    fullyUnlock();
                }
            }
    
            public void remove() {
                if (lastRet == null)
                    throw new IllegalStateException();
                fullyLock();
                try {
                    Node<E> node = lastRet;
                    lastRet = null;
                    for (Node<E> trail = head, p = trail.next;
                         p != null;
                         trail = p, p = p.next) {
                        if (p == node) {
                            unlink(p, trail);
                            break;
                        }
                    }
                } finally {
                    fullyUnlock();
                }
            }
        }
    
        /** A customized variant of Spliterators.IteratorSpliterator */
        static final class LBQSpliterator<E> implements Spliterator<E> {
            static final int MAX_BATCH = 1 << 25;  // max batch array size;
            final LinkedBlockingQueue<E> queue;
            Node<E> current;    // current node; null until initialized
            int batch;          // batch size for splits
            boolean exhausted;  // true when no more nodes
            long est;           // size estimate
            LBQSpliterator(LinkedBlockingQueue<E> queue) {
                this.queue = queue;
                this.est = queue.size();
            }
    
            public long estimateSize() { return est; }
    
            public Spliterator<E> trySplit() {
                Node<E> h;
                final LinkedBlockingQueue<E> q = this.queue;
                int b = batch;
                int n = (b <= 0) ? 1 : (b >= MAX_BATCH) ? MAX_BATCH : b + 1;
                if (!exhausted &&
                    ((h = current) != null || (h = q.head.next) != null) &&
                    h.next != null) {
                    Object[] a = new Object[n];
                    int i = 0;
                    Node<E> p = current;
                    q.fullyLock();
                    try {
                        if (p != null || (p = q.head.next) != null) {
                            do {
                                if ((a[i] = p.item) != null)
                                    ++i;
                            } while ((p = p.next) != null && i < n);
                        }
                    } finally {
                        q.fullyUnlock();
                    }
                    if ((current = p) == null) {
                        est = 0L;
                        exhausted = true;
                    }
                    else if ((est -= i) < 0L)
                        est = 0L;
                    if (i > 0) {
                        batch = i;
                        return Spliterators.spliterator
                            (a, 0, i, Spliterator.ORDERED | Spliterator.NONNULL |
                             Spliterator.CONCURRENT);
                    }
                }
                return null;
            }
    
            public void forEachRemaining(Consumer<? super E> action) {
                if (action == null) throw new NullPointerException();
                final LinkedBlockingQueue<E> q = this.queue;
                if (!exhausted) {
                    exhausted = true;
                    Node<E> p = current;
                    do {
                        E e = null;
                        q.fullyLock();
                        try {
                            if (p == null)
                                p = q.head.next;
                            while (p != null) {
                                e = p.item;
                                p = p.next;
                                if (e != null)
                                    break;
                            }
                        } finally {
                            q.fullyUnlock();
                        }
                        if (e != null)
                            action.accept(e);
                    } while (p != null);
                }
            }
    
            public boolean tryAdvance(Consumer<? super E> action) {
                if (action == null) throw new NullPointerException();
                final LinkedBlockingQueue<E> q = this.queue;
                if (!exhausted) {
                    E e = null;
                    q.fullyLock();
                    try {
                        if (current == null)
                            current = q.head.next;
                        while (current != null) {
                            e = current.item;
                            current = current.next;
                            if (e != null)
                                break;
                        }
                    } finally {
                        q.fullyUnlock();
                    }
                    if (current == null)
                        exhausted = true;
                    if (e != null) {
                        action.accept(e);
                        return true;
                    }
                }
                return false;
            }
    
            public int characteristics() {
                return Spliterator.ORDERED | Spliterator.NONNULL |
                    Spliterator.CONCURRENT;
            }
        }
    
        /**
         * Returns a {@link Spliterator} over the elements in this queue.
         *
         * <p>The returned spliterator is
         * <a href="package-summary.html#Weakly"><i>weakly consistent</i></a>.
         *
         * <p>The {@code Spliterator} reports {@link Spliterator#CONCURRENT},
         * {@link Spliterator#ORDERED}, and {@link Spliterator#NONNULL}.
         *
         * @implNote
         * The {@code Spliterator} implements {@code trySplit} to permit limited
         * parallelism.
         *
         * @return a {@code Spliterator} over the elements in this queue
         * @since 1.8
         */
        public Spliterator<E> spliterator() {
            return new LBQSpliterator<E>(this);
        }
    
        /**
         * Saves this queue to a stream (that is, serializes it).
         *
         * @param s the stream
         * @throws java.io.IOException if an I/O error occurs
         * @serialData The capacity is emitted (int), followed by all of
         * its elements (each an {@code Object}) in the proper order,
         * followed by a null
         */
        private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
    
            fullyLock();
            try {
                // Write out any hidden stuff, plus capacity
                s.defaultWriteObject();
    
                // Write out all elements in the proper order.
                for (Node<E> p = head.next; p != null; p = p.next)
                    s.writeObject(p.item);
    
                // Use trailing null as sentinel
                s.writeObject(null);
            } finally {
                fullyUnlock();
            }
        }
    
        /**
         * Reconstitutes this queue from a stream (that is, deserializes it).
         * @param s the stream
         * @throws ClassNotFoundException if the class of a serialized object
         *         could not be found
         * @throws java.io.IOException if an I/O error occurs
         */
        private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
            // Read in capacity, and any hidden stuff
            s.defaultReadObject();
    
            count.set(0);
            last = head = new Node<E>(null);
    
            // Read in all elements and place in queue
            for (;;) {
                @SuppressWarnings("unchecked")
                E item = (E)s.readObject();
                if (item == null)
                    break;
                add(item);
            }
        }
    }
