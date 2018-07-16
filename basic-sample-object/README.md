# 1. How to Computing an object's size
> [StackOverFlow Discuss](https://stackoverflow.com/questions/52353/in-java-what-is-the-best-way-to-determine-the-size-of-an-object)

# 2. Java Object在内存中的布局
> 对象头、实例数据、对齐填充
## 2.1 Object's Head structure
> [source code markOop.hpp](http://hg.openjdk.java.net/jdk8/jdk8/hotspot/file/87ee5ee27509/src/share/vm/oops/markOop.hpp)
 
> [Object's Header](https://wiki.openjdk.java.net/download/attachments/11829266/Synchronization.gif?version=4&modificationDate=1208918680000&api=v2)
 
> [Object's Head figure](./src/main/resources/Object-Header.png)

```text
The structure of the object header is defined in the classes oopDesc and markOopDesc, 
the code for thin locks is integrated in the interpreter and compilers, and the class ObjectMonitor represents inflated locks. 
Biased locking is centralized in the class BiasedLocking. It can be enabled via the flag -XX:+UseBiasedLocking and disabled via -XX:-UseBiasedLocking.
 It is enabled by default for Java 6 and Java 7, but activated only some seconds after the application startup. 
Therefore, beware of short-running micro-benchmarks. If necessary, turn off the delay using the flag -XX:BiasedLockingStartupDelay=0.
```
### 2.1.1 mark word
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


