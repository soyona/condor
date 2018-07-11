# AQS
> Reference:http://ifeve.com/introduce-abstractqueuedsynchronizer/

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
## 3 AQS 在exclusive mode下 获取锁
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
                    parkAndCheckInterrupt())
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
## AQS 在exclusive mode下 释放锁
```text
public final boolean release(int arg) {
        if (tryRelease(arg)) {
            Node h = head;
            if (h != null && h.waitStatus != 0)
                unparkSuccessor(h);
            return true;
        }
        return false;
}
```
> 参考 ifeve 总结的伪代码：
```text
if(释放锁成功){
    唤醒后驱节点，
}
```

