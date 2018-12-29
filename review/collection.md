# 集合
## 1.HashMap(JDK7)
    参考：https://github.com/553899811/NewBie-Plan/blob/master/Java%E5%9F%BA%E7%A1%80/Java-%E5%AE%B9%E5%99%A8/Map/HashMap.md
    参考：http://www.importnew.com/20386.html
    数组+链表；
    初始化桶：16个；最大值2^30，put时初始化
    桶的大小或者数组的大小：不是任意值，而是2的幂，通过tableSizeFor()方法得来
    扩容时机：16*0.75=12，扩容*2；
    链表转红黑树时：链表长度到达8时；
    红黑树转链表：链表节点长度较少到6时；
    JDK7：
    JDK8：引入红黑树
### 1.1 put
    判断是否需要扩容
        如果size==阈值,并且插入位置有元素,则扩容2倍,重新计算下标
    计算下标
    放入链表头部
### 1.2 get
    计算下标,hash值,hash&(length-1)
    遍历链表,找到相等的key(==||equals())
    
## 2.ConcurrentHashMap(JDK7)
    参考：http://www.importnew.com/23610.html
    参考：http://ifeve.com/concurrenthashmap/
    并发Map，采用分段锁（重入锁）
    JDK7：
    JDK8：

## 3.CopyOnWriteArrayList
    并发容器，读写分离思想，读时不用加锁，写时加锁（重入锁）
    加入元素时，先复制当前容器，将新元素加入新容器，然后将原容器的引用指向新容器；
    实现：ReentrantLock/Array.copyOf()/
    特点：可以在遍历时修改，普通集合在遍历时修改抛出异常；
    应用场景：读多写少的场景
    缺点：内存占用大，写时性能差