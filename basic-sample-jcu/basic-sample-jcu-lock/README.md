# 1 Java语言提供的同步机制
> 多个线程访问临界资源时，通过同步机制来处理这 "多个线程"，Java提供了两种同步机制：
- synchronized
- Lock

## 1.1 synchronized
>  被synchronized修饰的方法就是临界区，只允许一个线程访问该临界区；其他线程阻塞；
>  注意：被synchronized修饰的静态方法和普通方法是有区别的。
>  注意：synchronized的使用，让临界区尽量短。
### 1.1.1 synchronized实现生产者消费者
> https://github.com/soyona/condor/tree/master/basic-sample-jcu/basic-sample-jcu-app/src/main/java/jcu/app/producer_consummer
### 1.1.1 生产者消费者 为何使用while而不是if 
> spurious wakeup 虚假唤醒
> https://en.wikipedia.org/wiki/Spurious_wakeup
    >> 即使没有线程broadcast 或者signal条件变量，wait也可能返回。


## 1.2 Lock 接口
### 1.2.0 Introduce（翻译java doc）
```
Lock提供了比synchronized更多的扩展性的锁操作，提供更灵活的结构，可以做不同的属性，可以支持多个与所对象关联的Condition对象。
Lock是用来控制多线程访问共享资源的工具。一般来说，锁提供了对共享资源的互斥访问：同一时刻只能有一个线程获取锁，所有对该共享资源的访问都需要获取已经被获取的锁，
但是，一些锁允许并发访问共享资源，像ReadWriteLock的读锁。

synchronized方法或者语句的用法提供了对每个对象的隐式监视锁的访问，但是，它强制所有获取锁和释放锁的操作发生在块结构中：

lock的用法：
Lock l = ...;
l.lock();
try {
 // access the resource protected by this lock
} finally {
  l.unlock();
}

Lock还提供了其他功能，当尝试获取锁时可以被中断lockInterruptibly，并且，尝试获取锁可以超时tryLock(long, TimeUnit)
```
### 1.2.1 实现类
> java.util.concurrent.locks.Lock  has implements:
 
>> `->`java.util.concurrent.locks.ReentrantLock     
>> `->`java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock      
>> `->`java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock      
 
 
### 1.2.2 Lock接口方法
```java
public interface Lock {
    // 由子类ReentrantLock实现，ReentrantLock的lock由 内部类Sync的两个子类FairSync（公平锁）和NonFairSync(非公平锁)来实现
    void lock();
    // 如果锁不可用，disable当前线程，当前处于休眠状态，直到发生以下情况：
    // 1.如果锁被当前线程获取，或者其他线程interrupt当前线程，并且支持锁获取的中断。
    // 2.如果当前线程在进入该方法时已经有中断状态；或者在获取锁时被中断，并且支持锁获取的中断。
    // 被中断时，抛出InterruptedException
    void lockInterruptibly() throws InterruptedException;
    // 如果锁可用，立即返回true，否则，立即返回false
    boolean tryLock();
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
    //由AQS release()实现，release中调用子类的tryRelease()实现
    void unlock();
    Condition newCondition();
}
```

### 1.3 Lock与synchronized
- Lock灵活，lock() unlock() 配对，unlock()在finally{}中
- Lock加锁解锁都需手工，synchronized不需要
- Lock对代码块加锁，synchronized对对象加锁
- 当有多个读一个写时，Lock可以通过new Condition()实现读写分离

## 2 Lock子类ReentrantLock
>  ReentrantLock中锁的实现依赖Sync，Sync继承自AbstractQueuedSynchronizer  
>>  ReentrantLock.Sync extends AbstractQueuedSynchronizer   
 
>  Sync只提供抽象方法：
```
/**
 * Performs {@link Lock#lock}. The main reason for subclassing
 * is to allow fast path for nonfair version.
 */
abstract void lock();
```
> 其具体实现交由子类 FairSync 和  NonfairSync 来实现，子类的实现都会调用Sync的父类AbstractQueuedSynchronizer中的acquire(1)
 
> 请看AbstractQueuedSynchronizer中的acquire(1)
```
/**
 * Acquires in exclusive mode, ignoring interrupts.  Implemented
 * by invoking at least once {@link #tryAcquire},
 * returning on success.  Otherwise the thread is queued, possibly
 * repeatedly blocking and unblocking, invoking {@link
 * #tryAcquire} until success.  This method can be used
 * to implement method {@link Lock#lock}.
 *
 * @param arg the acquire argument.  This value is conveyed to
 *        {@link #tryAcquire} but is otherwise uninterpreted and
 *        can represent anything you like.
 */
public final void acquire(int arg) {
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}
``` 
> if (!tryAcquire(arg) &&acquireQueued(addWaiter(Node.EXCLUSIVE), arg)) 中tryAcquire(arg)分别由孙子类FairSync和NonfairSync实现获取锁逻辑
 
> 如果获取不成功，线程入队尾acquireQueued(addWaiter(Node.EXCLUSIVE), arg)，该逻辑由AbstractQueuedSynchronizer中实现：

> acquireQueued()
```text
    final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
```
> addWaiter，把当前线程放入队列中
```
private Node addWaiter(Node mode) {
   Node node = new Node(Thread.currentThread(), mode);
   // Try the fast path of enq; backup to full enq on failure
   Node pred = tail;// 获取尾节点
   if (pred != null) {// 如果尾节点为null，说明还有线程在等待
       node.prev = pred;// 当前节点 的前驱 指向尾节点tail
       if (compareAndSetTail(pred, node)) {// CAS, 修改tail指向 当前节点
           pred.next = node;
           return node;
       }
   }
   enq(node);// 队列中有线程等待，或者 前者CAS不成功时，执行该行
   return node;
}

```
> enq(final Node node),入队（循环直到成功入队）
```text
private Node enq(final Node node) {
    for (;;) { // 循环-直到插入成功为止
        Node t = tail;// 队列为空时，head = tail = null
        if (t == null) { // Must initialize，创建新节点初始化 tail = head = new Node()
            if (compareAndSetHead(new Node()))
                tail = head;
        } else { // 队列不为null，
            node.prev = t; // 当前节点的前驱 指向 队列tail
            if (compareAndSetTail(t, node)) { // CAS 设置 tail 指向当前节点
                t.next = node; // tail 的后驱节点 指向 当前节点
                return t; //返回原tail
            }
        }
    }
}
```
### 2.1  ReentrantLock.lock()-FairSync实现
> ReentrantLock.lock()，由Sync实现，向下看
```text
public void lock() {
    sync.lock();
}
```
> Sync.lock()，抽象方法，有子类实现，向下看
```text
abstract void lock();
```
> Sync子类 FairSync.lock()，向下看
```
final void lock() {
     acquire(1);// 爷AQS实现，向下看 
}
```
> AQS.acquire(int arg)，向下看
```
public final void acquire(int arg) {
        // tryAcquire(arg)子类实现
        if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))//如果当前线程没有获得锁，加入队列
            selfInterrupt();
    }
```
> FairSync.tryAcquire(int acquires)
```
/**
 * Fair version of tryAcquire.  Don't grant access unless
 * recursive call or no waiters or is first.
 */
protected final boolean tryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {// 说明没有其他线程占用锁
        if (!hasQueuedPredecessors() &&
            compareAndSetState(0, acquires)) {// 队列中没有线程等待，通过CAS设置state为acquires
            setExclusiveOwnerThread(current);//state状态设置成功，接着设置当前线程为锁的占有者 exclusiveOwnerThread=current
            return true;
        }
    }
    //如果c != 0，说明有其他线程占有锁，
    else if (current == getExclusiveOwnerThread()) {//如果锁占有线程 就是当前线程，重入，增加state值
        int nextc = c + acquires;
        if (nextc < 0)// 如果将要设置的state值小于0，抛出异常
            throw new Error("Maximum lock count exceeded");
        setState(nextc);//设置
        return true;
    }
    return false;
}
```

```
public final boolean hasQueuedPredecessors() {
    // The correctness of this depends on head being initialized
    // before tail and on head.next being accurate if the current
    // thread is first in queue.
    Node t = tail; // Read fields in reverse initialization order
    Node h = head;
    Node s;
    return h != t &&
        ((s = h.next) == null || s.thread != Thread.currentThread());
}
```
### 2.2  ReentrantLock.unlock()-FairSync实现
> ReentrantLock.unlock()，由内部类Sync实现
```text
    public void unlock() {
        sync.release(1);// AQS.release()
    }
```
> AQS.release()，其中tryRelease(arg)由子类实现,unparkSuccessor(h)由 AQS实现
```text
public final boolean release(int arg) {
    if (tryRelease(arg)) {// 释放成功，如果队列head节点不为null 并且head状态!=0,唤醒后续节点
        Node h = head;
        if (h != null && h.waitStatus != 0)
            unparkSuccessor(h);// 唤醒后续节点
        return true;
    }
    return false;
}
```
> Sync.tryRelease();
```text
protected final boolean tryRelease(int releases) {
    int c = getState() - releases;//释放锁后的state
    if (Thread.currentThread() != getExclusiveOwnerThread())// 如果当前线程不是owner线程，抛出异常
        throw new IllegalMonitorStateException();
    //单线程 ----begin
    boolean free = false;
    if (c == 0) {// 如果释放锁后的state 是0
        free = true;// 成功释放标志
        setExclusiveOwnerThread(null);//owner设置为null
    }
    setState(c);//设置状态
    return free;
    //单线程 ----end
}
```
> AQS.unparkSuccessor(h)
```text
private void unparkSuccessor(Node node) {
        /*
         * If status is negative (i.e., possibly needing signal) try
         * to clear in anticipation of signalling.  It is OK if this
         * fails or if status is changed by waiting thread.
         */
        int ws = node.waitStatus;
        if (ws < 0)// 如果等待状态小于0，设置为0
            compareAndSetWaitStatus(node, ws, 0);

        /*
         * Thread to unpark is held in successor, which is normally
         * just the next node.  But if cancelled or apparently null,
         * traverse backwards from tail to find the actual
         * non-cancelled successor.
         */
        Node s = node.next; 
        if (s == null || s.waitStatus > 0) {//如果后续节点为空
            s = null;
            for (Node t = tail; t != null && t != node; t = t.prev)//遍历找到状态为 小于等于 0 的线程唤醒
                if (t.waitStatus <= 0)
                    s = t;
        }
        if (s != null)
            LockSupport.unpark(s.thread);//LockSupport实现
    }
```

### 2.3  NonfairSync非公平锁实现 

> ReentrantLock.NonfairSync extends Sync

#### 2.2.1 lock()
```
/**
 * Performs lock.  Try immediate barge, backing up to normal
 * acquire on failure.
 */
final void lock() {
    if (compareAndSetState(0, 1))
        setExclusiveOwnerThread(Thread.currentThread());
    else
        acquire(1);
}
```
### 2.3  ReentrantLock.unlock()-NonfairSync实现
#### 2.3.1 release()

## 3 ReadWriteLock 接口
> java.util.concurrent.locks.ReadWriteLock 子类
>> `->` java.util.concurrent.locks.ReentrantReadWriteLock
### 3.1 接口方法
```java
public interface ReadWriteLock {
    Lock readLock();
    Lock writeLock();
}
```
 
> 实现例子：https://github.com/soyona/condor/tree/master/basic-sample-jcu/basic-sample-jcu-lock/src/main/java/sample/jcu/lock/readwritelock

### 3.2 实现类：ReentrantReadWriteLock
> 实现：基于AQS实现，在一个整型变量上维护多种状态，state=c，c的高16为用于读锁，c的低16用于写锁  
> CAS实现：  
>> 写锁：compareAndSetState(c, c + 1)  
>> 读锁：compareAndSetState(c, c + SHARED_UNIT) ,
>>> 读锁增加一个，需增加一个SHARED_UNIT（c+SHARED_UNIT）/int=65536,
>>> 若只有一个读锁，那么c=SHARED_UNIT（0000 0000 0000 000`1` 0000 0000 0000 0000）
>>> 获取读锁数量：通过 c >>> SHARED_SHIFT, 结果为：1（0000 0000 0000 0000 0000 0000 0000 000`1`） 

#### ReentrantReadWriteLock.Sync 内部类 

```java
    //表示读锁占用的位数
    static final int SHARED_SHIFT   = 16;
    //增加一个读锁,compareAndSetState(c, c + SHARED_UNIT),需要增加的单位是SHARED_UNIT
    static final int SHARED_UNIT    = (1 << SHARED_SHIFT);
    //最大的读锁数量 ：0000 0000 0000 0000 1111 1111 1111 1111
    static final int MAX_COUNT      = (1 << SHARED_SHIFT) - 1;
    //写锁掩码 16个1，写锁的数量：c & EXCLUSIVE_MASK
    static final int EXCLUSIVE_MASK = (1 << SHARED_SHIFT) - 1;
    
    /** Returns the number of shared holds represented in count  */
    static int sharedCount(int c)    { return c >>> SHARED_SHIFT; }
    /** Returns the number of exclusive holds represented in count  */
    static int exclusiveCount(int c) { return c & EXCLUSIVE_MASK; }
```   
> static final int SHARED_UNIT    = (1 << SHARED_SHIFT);
 
```text
0000 0000 0000 0000 0000 0000 0000 0001    1

0000 0000 0000 0001 0000 0000 0000 0000    int SHARED_UNIT    = (1 << SHARED_SHIFT)

0000 0000 0000 0000 1111 1111 1111 1111    int MAX_COUNT      = (1 << SHARED_SHIFT) - 1

0000 0000 0000 0000 1111 1111 1111 1111    int EXCLUSIVE_MASK = (1 << SHARED_SHIFT) - 1;  

```
> ReentrantReadWriteLock.Sync.tryWriteLock

```java
    /**
         * Performs tryLock for write, enabling barging in both modes.
         * This is identical in effect to tryAcquire except for lack
         * of calls to writerShouldBlock.
         */
    final boolean tryWriteLock() {
        Thread current = Thread.currentThread();
        int c = getState();
        if (c != 0) {
            int w = exclusiveCount(c);
            if (w == 0 || current != getExclusiveOwnerThread())
                return false;
            if (w == MAX_COUNT)
                throw new Error("Maximum lock count exceeded");
        }
        if (!compareAndSetState(c, c + 1))
            return false;
        setExclusiveOwnerThread(current);
        return true;
    }
```
> ReentrantReadWriteLock.Sync.tryReadLock

```text
    /**
         * Performs tryLock for read, enabling barging in both modes.
         * This is identical in effect to tryAcquireShared except for
         * lack of calls to readerShouldBlock.
         */
    final boolean tryReadLock() {
        Thread current = Thread.currentThread();
        for (;;) {
            int c = getState();
            if (exclusiveCount(c) != 0 &&
                getExclusiveOwnerThread() != current)
                return false;
            int r = sharedCount(c);
            if (r == MAX_COUNT)
                throw new Error("Maximum lock count exceeded");
            if (compareAndSetState(c, c + SHARED_UNIT)) {
                if (r == 0) {
                    firstReader = current;
                    firstReaderHoldCount = 1;
                } else if (firstReader == current) {
                    firstReaderHoldCount++;
                } else {
                    HoldCounter rh = cachedHoldCounter;
                    if (rh == null || rh.tid != getThreadId(current))
                        cachedHoldCounter = rh = readHolds.get();
                    else if (rh.count == 0)
                        readHolds.set(rh);
                    rh.count++;
                }
                return true;
            }
        }
    }
```

> ReentrantReadWriteLock.Sync.tryRelease

```java
/*
         * Note that tryRelease and tryAcquire can be called by
         * Conditions. So it is possible that their arguments contain
         * both read and write holds that are all released during a
         * condition wait and re-established in tryAcquire.
         */

        protected final boolean tryRelease(int releases) {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            int nextc = getState() - releases;
            boolean free = exclusiveCount(nextc) == 0;
            if (free)
                setExclusiveOwnerThread(null);
            setState(nextc);
            return free;
        }
```
> ReentrantReadWriteLock.Sync.tryAcquire
``` 
        protected final boolean tryAcquire(int acquires) {
            /*
             * Walkthrough:
             * 1. If read count nonzero or write count nonzero
             *    and owner is a different thread, fail.
             * 2. If count would saturate, fail. (This can only
             *    happen if count is already nonzero.)
             * 3. Otherwise, this thread is eligible for lock if
             *    it is either a reentrant acquire or
             *    queue policy allows it. If so, update state
             *    and set owner.
             */
            Thread current = Thread.currentThread();
            int c = getState();
            int w = exclusiveCount(c);
            if (c != 0) {
                // (Note: if c != 0 and w == 0 then shared count != 0)
                if (w == 0 || current != getExclusiveOwnerThread())
                    return false;
                if (w + exclusiveCount(acquires) > MAX_COUNT)
                    throw new Error("Maximum lock count exceeded");
                // Reentrant acquire
                setState(c + acquires);
                return true;
            }
            if (writerShouldBlock() ||
                !compareAndSetState(c, c + acquires))
                return false;
            setExclusiveOwnerThread(current);
            return true;
        }
```        
> ReentrantReadWriteLock.Sync.tryReleaseShared
```
        protected final boolean tryReleaseShared(int unused) {
            Thread current = Thread.currentThread();
            if (firstReader == current) {
                // assert firstReaderHoldCount > 0;
                if (firstReaderHoldCount == 1)
                    firstReader = null;
                else
                    firstReaderHoldCount--;
            } else {
                HoldCounter rh = cachedHoldCounter;
                if (rh == null || rh.tid != getThreadId(current))
                    rh = readHolds.get();
                int count = rh.count;
                if (count <= 1) {
                    readHolds.remove();
                    if (count <= 0)
                        throw unmatchedUnlockException();
                }
                --rh.count;
            }
            for (;;) {
                int c = getState();
                int nextc = c - SHARED_UNIT;
                if (compareAndSetState(c, nextc))
                    // Releasing the read lock has no effect on readers,
                    // but it may allow waiting writers to proceed if
                    // both read and write locks are now free.
                    return nextc == 0;
            }
        }
```        
> ReentrantReadWriteLock.Sync.tryAcquireShared  
```  
protected final int tryAcquireShared(int unused) {
            /*
             * Walkthrough:
             * 1. If write lock held by another thread, fail.
             * 2. Otherwise, this thread is eligible for
             *    lock wrt state, so ask if it should block
             *    because of queue policy. If not, try
             *    to grant by CASing state and updating count.
             *    Note that step does not check for reentrant
             *    acquires, which is postponed to full version
             *    to avoid having to check hold count in
             *    the more typical non-reentrant case.
             * 3. If step 2 fails either because thread
             *    apparently not eligible or CAS fails or count
             *    saturated, chain to version with full retry loop.
             */
            Thread current = Thread.currentThread();
            int c = getState();
            if (exclusiveCount(c) != 0 &&
                getExclusiveOwnerThread() != current)
                return -1;
            int r = sharedCount(c);
            if (!readerShouldBlock() &&
                r < MAX_COUNT &&
                compareAndSetState(c, c + SHARED_UNIT)) {
                if (r == 0) {
                    firstReader = current;
                    firstReaderHoldCount = 1;
                } else if (firstReader == current) {
                    firstReaderHoldCount++;
                } else {
                    HoldCounter rh = cachedHoldCounter;
                    if (rh == null || rh.tid != getThreadId(current))
                        cachedHoldCounter = rh = readHolds.get();
                    else if (rh.count == 0)
                        readHolds.set(rh);
                    rh.count++;
                }
                return 1;
            }
            return fullTryAcquireShared(current);
        }
```
>> 源码分析参考：https://blog.csdn.net/prestigeding/article/details/53286756

# 5 AbstractQueuedSynchronizer 抽象类
> java.util.concurrent.locks.AbstractQueuedSynchronizer 子类
- ReentrantLock
>> `->`java.util.concurrent.locks.ReentrantLock.Sync     
>>> `->`java.util.concurrent.locks.ReentrantLock.NonfairSync    
>>> `->`java.util.concurrent.locks.ReentrantLock.FairSync
- ReentrantReadWriteLock     
>> `->`java.util.concurrent.locks.ReentrantReadWriteLock.Sync  
>>> `->`java.util.concurrent.locks.ReentrantReadWriteLock.FairSync  
>>> `->`java.util.concurrent.locks.ReentrantReadWriteLock.NonfairSync  


