# 有关锁 
> Reference: http://ifeve.com/java_lock_see/
## 1 自旋锁
> Reference: http://ifeve.com/java_lock_see1/
 
> [代码分析](https://github.com/soyona/condor/blob/master/basic-sample-jcu/basic-sample-jcu-lock/src/main/java/sample/jcu/lock/SpinLock.java)
## 2 自旋锁的种类
## 3 阻塞锁
## 4 可重入锁
### 4.1 Synchronized 内置锁
```text
实现：每个锁关联一个计数器和Owner线程，重入时，判断Owner线程是否是当前线程。
```
### 4.1 ReentrantLock 可重入
```text
    /**
     * The current owner of exclusive mode synchronization.
     */
    private transient Thread exclusiveOwnerThread;
```
##### ReentrantLock
## 5 独占锁
##### 5.1 ReentrantLock
##### 5.2 ReentrantReadWriteLock.WriteLock
## 6 共享锁
##### 6.1 ReentrantReadWriteLock.ReadLock
##### 6.2 CyclicBarrier
##### 6.3 CountDownLatch
##### 6.4 Semaphore
## 7 读写锁
## 8 互斥锁(独占)
## 9 悲观锁
## 10 乐观锁
## 11 公平锁
> 定义：在绝对时间上，先对锁请求的线程先被满足，即等待时间最长的线程（队列头）最有机会获取锁，是有序的。
 
> 性能：
```
    公平锁没有非公平锁效率高，它没有考虑操作系统对线程的调度因素，
这样造成JVM对与等待中的线程的调度次序和操作系统的线程调度不匹配。
 
    连续获取的概率是非常高的，公平锁会压制这种情况。
虽然公平性得以保证，但是响应比却下降了，但是并不是任何场景都是以TPS作为唯一的指标的，因为
公平锁能够减少`饥饿`发生的概率，等待越久的请求越是能够得到满足。
```
### 11.1 ReentrantLock.FairSync
## 12 非公平锁
> 定义：无序的，不论是不是在队列头都能获取锁。相对`公平锁`的定义。

### 12.1 ReentrantLock.NonFairSync 
> ReentrantLock 默认为非公平锁 

> ReentrantLock 非公平锁 获取 实现：
```java
/**
 * Performs non-fair tryLock.  tryAcquire is implemented in
 * subclasses, but both need nonfair try for trylock method.
 */
final boolean nonfairTryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {
        if (compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}
```
## 13 偏向锁
## 14 对象锁
## 15 线程锁
## 16 锁粗化
## 17 轻量级锁
## 18 锁消除
## 19 锁膨胀
## 20 信号量
