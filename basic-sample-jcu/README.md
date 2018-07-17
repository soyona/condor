# JCU's Basic component
## 1. Synchronized
> [Kanglei's Understanding](./basic-sample-jcu-lock/synchronized.md)
### 1.1 等待队列：ContentionList
> Lock-Free队列，一个虚拟队列，通过链表指针连接，是一个LIFO队列，新节点加入头，通过CAS改变头节点，从队尾获取节点
### 1.2 等待队列：EntryList
```text
EntryList与ContentionList逻辑上同属等待队列；
Owner线程在unlock时会从ContentionList中迁移线程到EntryList，
，并会指定EntryList中的某个线程（一般为Head）为Ready（OnDeck）线程。
Owner线程并不是把锁传递给OnDeck线程，只是把竞争锁的权利交给OnDeck，OnDeck线程需要重新竞争锁。
```
### 1.3 底层队列 WaitSet
```
如果Owner线程被wait方法阻塞，则转移到WaitSet队列；
如果在某个时刻被notify/notifyAll唤醒，则再次转移到EntryList。
```
### 1.3 偏向锁
```text
等待队列都需要CAS操作，CAS操作会延迟本地调用
```
> 为了避免CAS操作
```text
对于获取偏向锁的线程，重入时 不需要CAS操作
```
### 1.4 自旋锁
```text
处于ContetionList、EntryList、WaitSet中的线程均处于阻塞状态，线程阻塞需要内核态参与，有上下文切换开销，
为了减少以上开销：采用自旋，当发生争用时，采用适应性锁，来根据锁需被持有的时间长短来采用自旋还是等待策略；
```
### 1.5 CAS(Lock-Free)

## 2. Lock
## 2.1 CLH
## 2.2 CAS(Lock-Free)

## 3. LockSupport
> [LockSupport Understanding](./basic-sample-jcu-lock/LockSupport.md)

## 4. Unsafe
> [Unsafe Understanding](./basic-sample-jcu-unsafe/README.md)

## 5. AQS
> [AQS Understanding](./basic-sample-jcu-lock/AQS.md)