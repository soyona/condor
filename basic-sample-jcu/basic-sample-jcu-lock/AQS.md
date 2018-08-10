# AQS
> Reference:http://ifeve.com/introduce-abstractqueuedsynchronizer/
> Reference:http://ifeve.com/jdk1-8-abstractqueuedsynchronizer-part1/
> Reference:http://ifeve.com/jdk1-8-abstractqueuedsynchronizer-part2/

##  基于FIFO队列实现
```text
CLH队列 -- Craig, Landin, and Hagersten lock queue
CLH队列是AQS中“等待锁”的线程队列。在多线程中，为了保护竞争资源不被多个线程同时操作而起来错误，我们常常需要通过锁来保护这些资源。在独占锁中，竞争资源在一个时间点只能被一个线程锁访问；而其它线程则需要等待。CLH就是管理这些“等待锁”的线程的队列。
CLH是一个非阻塞的 FIFO 队列。也就是说往里面插入或移除一个节点的时候，在并发条件下不会阻塞，而是通过自旋锁和 CAS 保证节点插入和移除的原子性。
```
### FIFO队列图示 
> ![FIFO](https://github.com/soyona/condor/blob/master/basic-sample-jcu/basic-sample-jcu-lock/src/main/resources/AQS-FIFO.jpeg) 

### FIFO队列元素 Node
> Node用来保存线程引用，线程状态
```text
static final class Node {
        /** Marker to indicate a node is waiting in shared mode */
        static final Node SHARED = new Node();
        /** Marker to indicate a node is waiting in exclusive mode */
        static final Node EXCLUSIVE = null;

        /** waitStatus value to indicate thread has cancelled */
        static final int CANCELLED =  1;
        /** waitStatus value to indicate successor's thread needs unparking */
        static final int SIGNAL    = -1;
        /** waitStatus value to indicate thread is waiting on condition */
        static final int CONDITION = -2;
        /**
         * waitStatus value to indicate the next acquireShared should
         * unconditionally propagate
         */
        static final int PROPAGATE = -3;
        volatile int waitStatus;
        volatile Node prev;
        volatile Node next;
        volatile Thread thread;
        Node nextWaiter;//存储condition队列中的后继节点
}
``` 

## AQS 三个成员变量

```text
//头节点
private transient volatile Node head;
//尾节点
private transient volatile Node tail;
//状态
private volatile int state;
```

## AQS 对状态修改的方法
> 1.getState() 

```text
protected final int getState() {
        return state;
    }
```
> 2.setState(int newState)
```
protected final void setState(int newState) {
        state = newState;
    }
```    
> 3.compareAndSetState(int expect, int update)
``` 
protected final boolean compareAndSetState(int expect, int update) {
        // See below for intrinsics setup to support this
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }
```
## 3.1 AQS 在exclusive mode下 获取锁
```text
public final void acquire(int arg) {
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}
```
> acquire过程分析：
```text
1. 尝试获取锁，
2. 获取失败，构造Node节点，加入Sync队列
2.1 for(;;)中，再次尝试获取锁，
2.1.1 如果成功 设置为头节点，跳出循环；
2.1.2 否则：LockSupport.park(this);//Disables the current thread for thread scheduling purposes unless the permit is available. lies dormant until....
2.2 中断 Thread.currentThread().interrupt();

```
### 第一步：尝试获取锁，tryAcquire

### 第二步：尝试失败，入队 addWaiter
```text
private Node addWaiter(Node mode) {
    Node node = new Node(Thread.currentThread(), mode);
    // Try the fast path of enq; backup to full enq on failure
    Node pred = tail;
    // 如果为节点不为空，在队列尾部添加----->
    if (pred != null) {
        node.prev = pred;//新节点前驱指向队列尾节点
        if (compareAndSetTail(pred, node)) {// 更新队列的tail，原子性保证
            pred.next = node;//尾节点的后驱指向新节点
            return node;
        }
    }
    // 如果尾节点为空，进入enq
    enq(node);
    return node;
}

/**
* for(;;) 循环 直到 入队成功，
**/
private Node enq(final Node node) {
        for (;;) {
            Node t = tail;
            if (t == null) { // Must initialize，如果tail节点null，初始化队列，
                if (compareAndSetHead(new Node()))
                    tail = head;
            } else { // 如果队列不为空，新节点的前驱 指向 尾节点，
                node.prev = t;
                if (compareAndSetTail(t, node)) {// 原子性（更新 队列 tail 节点指向，原尾节点后继节点指向新节点），CAS 保证原子性，防止其他线程同时更新，
                    t.next = node;
                    //更新成功，返回原尾节点，跳出循环
                    return t;
                }
                //CAS不成功，继续循环，直到成功
            }
        }
    }
```
### 第三步： 入队线程获取锁，处理 获取成功和失败的 线程acquireQueued
> acquireQueued 分析：对于已经入队的线程 以exclusive and uninterruptible mode 获取锁
```text
 该方法的作用，是入队的每个线程形成自旋行为：
 1、要么在自旋中永生（获取锁-结束自旋）；
 1.1 获取锁后 设置为head节点
 2、要么在自旋中沉沦（park等待被唤醒，继续自旋）；
 
```

```text
final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            // for(;;)自旋，退出条件：直到获取锁，或者 获取锁失败后 被park的线程 被唤醒
            for (;;) {
                final Node p = node.predecessor(); //1.1 获取当前节点的前驱节点
                if (p == head && tryAcquire(arg)) {//1.2 如果当前节点是头节点，并且获取锁，
                    setHead(node);// 1.3 设置当前节点为 头节点
                    p.next = null; // help GC  1.4 清除 对当前节点的引用
                    failed = false;
                    return interrupted;
                }
                //2. 如果当前节点的 前驱节点 不是头节点，也就是没有获取锁，当前线程节点睡眠，禁止调度，
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())//parkAndCheckInterrupt 是等待唤醒，由release方法中的 LockSupport.unpark(t)唤醒
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
```
> [LockSupport](./LockSupport.md)

> 参考 ifeve 总结的伪代码：
```text
if(获取锁失败 && 加入队列){
    中断当前线程
}
```
## 3.2 AQS 在exclusive mode下 释放锁
```text
/**
* 尝试释放锁，成功 则 唤醒FIFO队列中头节点的后继节点。
*
**/
public final boolean release(int arg) {
        if (tryRelease(arg)) {// 尝试释放锁，由子类实现
            Node h = head;
            if (h != null && h.waitStatus != 0)// 1. 如果 头节点不为null
                unparkSuccessor(h);// 1.1 唤醒头节点的 后继节点
            return true;
        }
        // 释放不成功，返回false
        return false;
}


/**
 * Wakes up node's successor, if one exists.
 *
 * @param node the node
 */
private void unparkSuccessor(Node node) {
    /*
     * If status is negative (i.e., possibly needing signal) try
     * to clear in anticipation of signalling.  It is OK if this
     * fails or if status is changed by waiting thread.
     */
    int ws = node.waitStatus;// 获取node节点的等待状态
    if (ws < 0)// 如果状态小于0，
        compareAndSetWaitStatus(node, ws, 0);//设置node等待状态

    /*
     * Thread to unpark is held in successor, which is normally
     * just the next node.  But if cancelled or apparently null,
     * traverse backwards from tail to find the actual
     * non-cancelled successor.
     */
    Node s = node.next; // 获取node的后继节点
    //找出可被唤醒的后继节点（那些状态 waitStatus<=0 的node）
    if (s == null || s.waitStatus > 0) { // 如果后继节点为null，或者 后继节点的状态大于0
        s = null;
        for (Node t = tail; t != null && t != node; t = t.prev)
            if (t.waitStatus <= 0)
                s = t;
    }
    // 找到可被唤醒的节点，唤醒该节点线程
    if (s != null)
        LockSupport.unpark(s.thread);
}
```

## 4.1 AQS Shared mode下获取锁
```text
区别独占模式：
1. 如果某一线程 进行读操作，那么 其他写操作阻塞
2. 如果某一线程 进行读操作，那么 其他读操作可获取锁
3. 如果某一线程 进行写操作，那么 其他读操作阻塞，写操作阻塞
```

```text
public final void acquireShared(int arg) {
        if (tryAcquireShared(arg) < 0)//尝试获取共享状态，自旋模式，非阻塞
            doAcquireShared(arg);// 如果获取失败，加入队列，开启自旋
}


private void doAcquireShared(int arg) {
    final Node node = addWaiter(Node.SHARED);//当前线程加入队列
    boolean failed = true;
    try {
        boolean interrupted = false;
        // 自旋
        for (;;) {
            final Node p = node.predecessor();// 获取当前节点的前驱节点
            if (p == head) { // 如果其前驱节点是头节点
                int r = tryAcquireShared(arg);//再次尝试获取锁
                if (r >= 0) {//如果获取锁成功，退出自旋
                    setHeadAndPropagate(node, r);
                    p.next = null; // help GC
                    if (interrupted)
                        selfInterrupt();
                    failed = false;
                    return;
                }
                // 如果获取锁失败，往下执行，阻塞，等待唤醒
            }
            //1.如果前驱节点不是头节点，阻塞，等待唤醒
            //2.如果获取锁失败，往下执行，阻塞，等待唤醒
            //2.1 这里的阻塞通过 LockSupport.park()实现
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

## 4.2 AQS Shared mode下释放锁
```text
public final boolean releaseShared(int arg) {
    if (tryReleaseShared(arg)) {//尝试释放锁，
        doReleaseShared();
        return true;
    }
    return false;
}

/**
* 自旋，找出唤醒哪个线程节点
*
*/
private void doReleaseShared() {
    for (;;) {
        Node h = head;// 获取
        if (h != null && h != tail) {//如果头节点不为null 并且不等于tail节点
            int ws = h.waitStatus;
            if (ws == Node.SIGNAL) {//Y:如果头节点是Node.SIGNAL状态，唤醒该节点
                if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0))//如果设置状态不成功，continue，继续自旋
                    continue;            // loop to recheck cases
                unparkSuccessor(h);// 唤醒后继节点
            }
            //N: 如果头节点状态 ==0，且设置状态不成功，continue，继续自旋
            else if (ws == 0 &&
                     !compareAndSetWaitStatus(h, 0, Node.PROPAGATE))
                continue;                // loop on failed CAS
        }
        //如果头节点有更新，结束自旋
        if (h == head)                   // loop if head changed
            break;
    }
}
```
> 参考 ifeve 总结的伪代码：
```text
if(释放锁成功){
    唤醒后驱节点，
}
```

