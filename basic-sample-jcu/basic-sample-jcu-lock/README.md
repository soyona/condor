# Java语言提供的同步机制
> 多个线程访问临界资源时，通过同步机制来处理这 "多个线程"，Java提供了两种同步机制：
- synchronized
- Lock

## synchronized
>  被synchronized修饰的方法就是临界区，只允许一个线程访问该临界区；其他线程阻塞；
>  注意：被synchronized修饰的静态方法和普通方法是有区别的。
>  注意：synchronized的使用，让临界区尽量短。
### synchronized实现生产者消费者
> https://github.com/soyona/condor/tree/master/basic-sample-jcu/basic-sample-jcu-app/src/main/java/jcu/app/producer_consummer
### 生产者消费者 为何使用while而不是if

> spurious wakeup 虚假唤醒
> https://en.wikipedia.org/wiki/Spurious_wakeup
    >> 即使没有线程broadcast 或者signal条件变量，wait也可能返回。


## Lock 接口
> Introduce（翻译java doc）
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
> java.util.concurrent.locks.Lock  has implements:
 
>> `->`java.util.concurrent.locks.ReentrantLock     
>> `->`java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock      
>> `->`java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock      
 
 
> 接口方法
```java
public interface Lock {
    //
    void lock();
    // 如果锁不可用，disable当前线程，当前处于休眠状态，直到发生以下情况：
    // 1.如果锁被当前线程获取，或者其他线程interrupt当前线程，并且支持锁获取的中断。
    // 2.如果当前线程在进入该方法时已经有中断状态；或者在获取锁时被中断，并且支持锁获取的中断。
    // 被中断时，抛出InterruptedException
    void lockInterruptibly() throws InterruptedException;
    // 如果锁可用，立即返回true，否则，立即返回false
    boolean tryLock();
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
    void unlock();
    Condition newCondition();
}
```
### Lock与synchronized
- Lock灵活，lock() unlock() 配对，unlock()在finally{}中
- Lock加锁解锁都需手工，synchronized不需要
- Lock对代码块加锁，synchronized对对象加锁
- 当有多个读一个写时，Lock可以通过new Condition()实现读写分离

## ReadWriteLock 接口
> java.util.concurrent.locks.ReadWriteLock 子类
>> `->` java.util.concurrent.locks.ReentrantReadWriteLock
```java
public interface ReadWriteLock {
    Lock readLock();
    Lock writeLock();
}
```
 
> 实现例子：https://github.com/soyona/condor/tree/master/basic-sample-jcu/basic-sample-jcu-lock/src/main/java/sample/jcu/lock/readwritelock

### 子类：ReentrantReadWriteLock
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
# AbstractQueuedSynchronizer 抽象类
> java.util.concurrent.locks.AbstractQueuedSynchronizer 子类
- ReentrantLock
>> `->`java.util.concurrent.locks.ReentrantLock.Sync     
>>> `->`java.util.concurrent.locks.ReentrantLock.NonfairSync    
>>> `->`java.util.concurrent.locks.ReentrantLock.FairSync
- ReentrantReadWriteLock     
>> `->`java.util.concurrent.locks.ReentrantReadWriteLock.Sync  
>>> `->`java.util.concurrent.locks.ReentrantReadWriteLock.FairSync  
>>> `->`java.util.concurrent.locks.ReentrantReadWriteLock.NonfairSync  


