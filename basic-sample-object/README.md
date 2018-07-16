# 1. How to Computing an object's size
> [StackOverFlow Discuss](https://stackoverflow.com/questions/52353/in-java-what-is-the-best-way-to-determine-the-size-of-an-object)

# 2. Java Object在内存中的布局
> 对象头、实例数据、对齐填充
## 2.1 Object's Head structure
> [source code markOop.hpp](http://hg.openjdk.java.net/jdk8/jdk8/hotspot/file/87ee5ee27509/src/share/vm/oops/markOop.hpp)
 
> [Object's Header](https://wiki.openjdk.java.net/download/attachments/11829266/Synchronization.gif?version=4&modificationDate=1208918680000&api=v2)
```text
The structure of the object header is defined in the classes oopDesc and markOopDesc, 
the code for thin locks is integrated in the interpreter and compilers, and the class ObjectMonitor represents inflated locks. 
Biased locking is centralized in the class BiasedLocking. It can be enabled via the flag -XX:+UseBiasedLocking and disabled via -XX:-UseBiasedLocking.
 It is enabled by default for Java 6 and Java 7, but activated only some seconds after the application startup. 
Therefore, beware of short-running micro-benchmarks. If necessary, turn off the delay using the flag -XX:BiasedLockingStartupDelay=0.
```
> 
![Object's Head figure](https://github.com/soyona/condor/blob/master/basic-sample-object/src/main/resources/Object-Header.png)
![64 bits](https://github.com/soyona/condor/blob/master/basic-sample-object/src/main/resources/Object's64.png)
![128 bits](https://github.com/soyona/condor/blob/master/basic-sample-object/src/main/resources/Object's128.png)
![96 bits](https://github.com/soyona/condor/blob/master/basic-sample-object/src/main/resources/Object's96.png)

> 01->00 
```text
As long as an object is unlocked,the last two bits have the value 01.
When a method synchronizes on an object,
the header word and a pointer to the object are stored in a lock record within the current stack frame.
Then the VM attempts to install a pointer to the lock record in the object's header word via a compare-and-swap operation.
If it succeeds,the current thread aferwards owns the lock,
Since lock records are always aligned at word boundaries,
the last two bits of the header word are then 00 and identify the object as being locked.
```
```text
If the compare-and-swap operation fails because the object was locked before, 
the VM first tests whether the header word points into the method stack of the current thread. 
In this case, the thread already owns the object's lock and can safely continue its execution.
For such a recursively locked object, the lock record is initialized with 0 instead of the object's header word. 
Only if two different threads concurrently synchronize on the same object, 
the thin lock must be inflated to a heavyweight monitor for the management of waiting threads.
```
```text
The mark word has word size (4 byte on 32 bit architectures, 8 byte on 64 bit architectures) and
```
### 2.1.2 class pointer
```text
klass pointer has word size on 32 bit architectures. On 64 bit architectures the klass pointer either has word size, but can also have 4 byte 
if the heap addresses can be encoded in these 4 bytes.
```
## 2.2 实例数据
> 存放类的属性信息，包括父类属性信息，如果是数组还包括数组长度，这部分按照4个字节对齐
## 2.3 对齐填充
> 由于JVM要求对象的起始地址必须是8的整数倍，必须填充补齐


