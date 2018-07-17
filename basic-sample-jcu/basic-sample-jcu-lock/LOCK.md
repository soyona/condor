# 一、有关锁 
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
> 悲观思想：并发情况下，以为会被其他线程修改，因此每次读写数据先加锁，导致读写相同数据的其他线程阻塞直到锁释放。例如：Synchronized，ReentrantLock
## 10 乐观锁
> 乐观思想：并发情况下，读数据时不加锁，更新数据时对比原先读取版本号判断是否被其他线程修改。
### 10.1 CAS
### 10.2 JAVA偏向锁
### 10.3 JAVA轻量级锁
### 10.4 自旋锁
##### Note：偏向锁->轻量级锁->重量级锁 过程
> [参考](https://blog.csdn.net/zqz_zqz/article/details/70233767)
###### 偏向锁的获取过程：
```text
1. 访问Mark Word中的偏向锁表示是否为1，锁标志位是否为01，是则为偏向锁
2. 是偏向锁，判断Mark Word中Thread ID是否为当前线程：
    -> 2.1 如果是当前线程，执行同步代码
    -> 2.2 不是当前线程，则通过CAS操作竞争锁，
        -> 2.2.1 ：如果竞争成功，修改Mark Word 中ThreadID 为当前线程ID，
        -> 2.2.2 ：如果竞争失败，当到达全局安全点（safepoint）时获得偏向锁的线程被挂起，偏向锁升级为 轻量级锁  
```
###### 偏向锁的释放过程：
```text
在遇到其他线程竞争锁偏向锁时，持有偏向锁的线程才会释放锁，线程不会主动释放偏向锁，
偏向锁的释放需要等待全局安全点，它会首先暂停拥有偏向锁的线程，判断对象是否被锁定，恢复到未锁定或者轻量级锁状态
```
###### 轻量级锁的获取过程：
```text
1. 代码进入同步块时，如果对象锁的状态为无锁状态，虚拟机在当前栈帧中创建一个锁记录空间（Lock Record），用于存储锁对象的Mark Word拷贝（Displaced Mark Word），
2. 拷贝Mark Word到Lock Record中（当前栈帧）
3. 拷贝成功后，虚拟机将CAS修改所对象的Mark Word更新为Lock Record的指针，并将Lock Record中的Owner指向Object Mark Word，
    -> 3.1 如果更新成功：表示该线程获取该对象的锁，并将对象的Mark Word的锁标志设置为00
    -> 3.2 如果更新失败：判断该对象Mark Word是否指向当前线程的栈帧：
        -> 3.2.1 若是 表明该线程已经获取了该对象锁，进入同步块执行；
        -> 3.2.2 若否 表明该锁被多线程竞争，轻量级锁 此时 膨胀为重量级锁，锁标志变为 10，Mark Word指向重量级锁的指针，后面
                 等待锁的线程进入阻塞状态，当前线程尝试自旋获取锁。

```
###### 轻量级锁的释放过程 :
```text
由轻量锁切换到重量锁，是发生在轻量锁释放锁的期间，之前在获取锁的时候它拷贝了锁对象头的markword，在释放锁的时候如果它发现在它持有锁的期间有其他线程来尝试获取锁了，
并且该线程对markword做了修改，两者比对发现不一致，则切换到重量锁。
```
## 11 对象锁 （Object monitor）
## 12 线程锁
## 13 偏向锁（Biased Locking）
> Java偏向锁(Biased Locking)是Java6引入的一项多线程优化，而偏向锁是在无竞争场景下完全消除同步，
```text
偏向锁是虚拟机对锁实现所做的优化。这种优化基于这样的观测结果（Observation）：大多数锁并没有被争用（contented），并且这些锁在生命周期内只会被一个线程持有。
因为字节码指令：monitorenter/monitorexit执行时借助CAS操作，代价昂贵。
因此：虚拟机为每个对象维护一个偏好：bias，即一个对象的内部锁第一次被一个线程获得，那么这个线程就被标记为该对象的偏好线程。这个线程以后再次申请该锁/释放该锁，都无需借助CAS操作，
从而减少了锁申请和释放的开销。
```
```text
当一个对象的偏好线程之外的线程需要获取该对象的内部锁时，JVM需要收回（Revoke）并重新设置该对象的偏好线程。
由于 收回和设置也有开销，因此如果程序运行中存在比较多的锁争用的情况，开销随之增大；
因此，偏向锁优化只适合于存在相当大一部分锁并没有被争用的系统之中
```
```text
偏向锁优化默认是开启的。要关闭偏向锁优化，我们可以在Java程序的启动命令行中添加虚拟机参数“-XX:-UseBiasedLocking”（开启偏向锁优化可以使用虚拟机参数“-XX:+UseBiasedLocking”）。
```
## 14 锁粗化（Lock Coarsening/Lock Merging）
```text
锁粗化是JIT编译器对内部锁的具体实现，对于相邻的几个同步块，如果这些同步块使用的同一个锁实例，那么JIT编译器会将这些同步块合并为一个大同步块。
从而避免了一个线程反复申请、释放同一锁所导致的开销。
然而，锁粗化可能导致一个线程在同一锁上等待时间变长（临界区变长）
```
```text
锁粗化默认是开启的。如果要关闭这个特性，我们可以在Java程序的启动命令行中添加虚拟机参数“-XX:-EliminateLocks”
```
## 15 锁消除（Lock Elision）

```
JIT编译器通过`逃逸分析` （Escape Analysis）技术判断同步块所使用的锁对象是否只能够被一个线程访问，
如果分析证实同步块只会被一个线程访问，那么JIT编译器在编译这个同步块时并不生成Synchronized的对应的机器码monitorexit/monitorenter,
即：消除了锁的使用，被称为：锁消除。
```
> 逃逸分析
```text
`逃逸分析`:技术自Java SE 6u23起默认是开启的
```
```text
锁消除优化是在Java 7开始引入的，锁消除是JIT编译器而不是javac所做的一种优化，而一段代码只有在其被执行的频率足够大的情况下才有可能会被JIT编译器优化
```
> 例如：StringBuffer.append/toString/reverse等同步方法，StringBuffer变量在方法体中使用，只会单线程使用，不会发生并发
```text
    @Override
    public synchronized StringBuffer append(String str) {
        toStringCache = null;
        super.append(str);
        return this;
    }
```
## 16 适应性锁（Adaptive Locking/Adaptive Spinning）
> 定义：是JIT编译器对内部锁实现所做的一种优化。
 
> 使用时机：等待锁的策略
```text
在锁争用的情况下，如果一个线程获取正在被其他线程占有的锁时，该线程就需要等待直到锁被释放，实现这种等待有两种方式：
```
> 策略一：暂停（非Runnable状态）
```text
暂停导致上下文切换，这种策略比较适合系统中绝大多数线程对该锁持有时间较长的情况，这样可以减少上下文切换的开销。
```
> 策略二：忙等（Busy wait）
```text
//空操作，
while (lockIsHeldByOtherThread){}
```
```text
忙等可以减少上下文切换，但消耗CPU资源，因此，忙等策略适合绝大多数线程对该锁持有时间较短的场景，以避免处理器时间开销。
```
> 总结：
```text
对于具体锁实例，虚拟机可以根据运行过程中收集到的信息该锁是 持有时间"较长"还是"较短"。较长则采用 暂停策略，较短则采用忙等策略；因此成为 适应性锁。
```
## 17 轻量级锁
## 18 锁膨胀
## 19 信号量

## 20 公平锁
> 定义：在绝对时间上，先对锁请求的线程先被满足，即等待时间最长的线程（队列头）最有机会获取锁，是有序的。
 
> 性能：
```
    公平锁没有非公平锁效率高，它没有考虑操作系统对线程的调度因素，
这样造成JVM对与等待中的线程的调度次序和操作系统的线程调度不匹配。
 
    连续获取的概率是非常高的，公平锁会压制这种情况。
虽然公平性得以保证，但是响应比却下降了，但是并不是任何场景都是以TPS作为唯一的指标的，因为
公平锁能够减少`饥饿`发生的概率，等待越久的请求越是能够得到满足。
```
### 20.1 ReentrantLock.FairSync
## 21 非公平锁
> 定义：无序的，不论是不是在队列头都能获取锁。相对`公平锁`的定义。
### 21.0 Synchronized
### 21.1 ReentrantLock.NonFairSync 
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
## 22 死锁（Dead Lock）
```text
线程处于阻塞状态
```
## 23 活锁（Live Lock）
```text
线程一直处于运行状态，做无用功，消耗CPU资源
```
# 二、锁优化
## 2.1 减少锁的时间

## 2.2 较少锁的粒度
> 把一个锁拆成多个锁，减少锁竞争，增加并行
 
> 例如： 
### 2.2.1 ConcurrentHashMap
> JDK1.8之前 ConcurrentHashMap采用Segment数组
```text
   Segment< K,V >[] segments
    /**
     * Stripped-down version of helper class used in previous version,
     * declared for the sake of serialization compatibility
     */
    static class Segment<K,V> extends ReentrantLock implements Serializable {
        private static final long serialVersionUID = 2249069246763182397L;
        final float loadFactor;
        Segment(float lf) { this.loadFactor = lf; }
    }
```
### 2.2.2 LongAdder
```text
开始没有并发争用的时候或者是cells数组正在初始化的时候，会使用cas来将值累加到成员变量的base上，
在并发争用的情况下，LongAdder会初始化cells数组，
在Cell数组中选定一个Cell加锁，数组有多少个cell，就允许同时有多少线程进行修改，
最后将数组中每个Cell中的value相加，在加上base的值，就是最终的值；
cell数组还能根据当前线程争用情况进行扩容，初始长度为2，每次扩容会增长一倍，直到扩容到大于等于cpu数量就不再扩容，
这也就是为什么LongAdder比cas和AtomicInteger效率要高的原因，后面两者都是volatile+cas实现的，他们的竞争维度是1，
LongAdder的竞争维度为“Cell个数+1”为什么要+1？因为它还有一个base，如果竞争不到锁还会尝试将数值加到base上；
```
### 2.2.3 LinkedBlockingQueue
```text
LinkedBlockingQueue也体现了这样的思想，
在队列头入队，在队列尾出队，入队和出队使用不同的锁，
相对于LinkedBlockingArray只有一个锁效率要高；
```
### NOTE: 拆锁的粒度不能无限拆，最多可以将一个锁拆为当前cup数量个锁即可；

## 2.3 锁粗化
```text
假如有一个循环，循环内的操作需要加锁，我们应该把锁放到循环外面，否则每次进出循环，都进出一次临界区，效率是非常差的；
```
## 2.4 读写锁
```text
ReentrantReadWriteLock 是一个读写锁，读操作加读锁，可以并发读，写操作使用写锁，只能单线程写；
```
### 2.4.1 ReentrantReadWriteLock

## 2.5 读写分离
> CopyOnWrite容器是一个写时复制容器，添加元素时，先将原先容器复制，在新的容器中添加元素，再将原容器指向新容器
> 可以支持并发读，写时是新容器，读写分离
> 用于都多写少的情景，读时无需加锁，写时加锁，并发写时避免复制多个新容器。
### 2.5.1 CopyOnWriteArrayList 
### 2.5.2 CopyOnWriteArraySet
## 2.6 CAS
> 避免线程上线文切换
## 2.7 消除缓存行
```
由于CPU读写数据是以缓存行为单位，缓存行单位大小：32字节/64字节（32位CPU/64位CPU）
需要同步的变量可能和其他变量同时存在同一缓存行，其他变量会被连带加锁
```
> 为了消除缓存行：
```text
1.在jdk1.7之前会 将需要独占缓存行的变量前后添加一组long类型的变量，依靠这些无意义的数组的填充做到一个变量自己独占一个缓存行；
2.在jdk1.7因为jvm会将这些没有用到的变量优化掉，所以采用继承一个声明了好多long变量的类的方式来实现；
3.在jdk1.8中通过添加sun.misc.Contended注解来解决这个问题，若要使该注解有效必须在jvm中添加以下参数： 
  -XX:-RestrictContended
  
  sun.misc.Contended注解会在变量前面添加128字节的padding将当前变量与其他变量进行隔离；
```
