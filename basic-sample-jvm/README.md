垃圾回收监控
-----

 * IDEA配置一
```text

在IDEA->Run->Edit Configuration->选项 Configuration中->VM options: 配置内容如下：
-verbose:gc
-XX:+PrintGCDetails
-XX:+PrintGCDateStamps
-Xloggc:/Users/kanglei/GitHub/condor/basic-sample-jvm/gc.log

或者简化：
-verbose:gc
-Xloggc:/Users/kanglei/GitHub/condor/basic-sample-jvm/gc.log
```

```text
gc.log内容：

CommandLine flags: -XX:InitialHeapSize=268435456 -XX:MaxHeapSize=4294967296 -XX:+PrintGC -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseParallelGC 
0.124: [GC (System.gc())  69468K->66056K(251392K), 0.0007735 secs]
0.125: [Full GC (System.gc())  66056K->346K(251392K), 0.0041768 secs]
0.135: [GC (System.gc())  65882K->65882K(251392K), 0.0003951 secs]
0.136: [Full GC (System.gc())  65882K->65862K(251392K), 0.0111741 secs]


```
* IDEA配置二
```text
在IDEA->Run->Edit Configuration->选项 Configuration中->VM options: 配置内容如下：

-XX:+PrintGC
-XX:+PrintGCDetails
-XX:+PrintGCDateStamps
-Xloggc:/Users/kanglei/GitHub/condor/basic-sample-jvm/gc.log

```

运行时栈帧结构
---


-  局部变量表
```text
摘自：[JAVA虚拟机规范-P13]
1、每个栈帧内部都包含一组称为局部变量表的变量列表。栈帧中局部变量表的长度在编译期决定，并且存储于类或者接口的二进制表示之中
2、一个局部变量可以保存一个类型为boolean、byte、char、short、int、float、reference、或returnAddress的数据。两个局部变量可以保存一个long和double的数据。
3、局部变量表使用索引来进行定位访问，从0开始访问。
4、Java虚拟机使用局部变量表来完成方法调用时的参数传递。当调用一个方法时，它的参数将会传递至从0开始的连续的局部变量表位置上。
4.1、当调用一个实例方法时，第0个局部变量一定是用来存储被调用的实例方法所在的对象的引用（Java语言中的this关键字）。后续的其他参数将会传递至从1开始的连续的局部变量位置上。
```
##### 问题：没有指明局部变量表每个局部变量的单位大小

```text
摘自：[深入理解Java虚拟机-P238]
1、局部变量表（Local Variable Table）是一个组变量值存储空间，用于存放方法参数和方法内部定义的局部变量。
2、局部变量表的容量以变量槽（Variable Slot，下称Slot）为最小单位，虚拟机规范中并没有明确指明一个Slot应占用的内存大小。
3、一个Slot可以存放32位以内的数据类型。Java占用32位以内的数据类型有boolean、byte、char、short、int、float、refrence和returnAddress 8种类型。
4、对于64位的数据类型，虚拟机会以高位对齐的方式为其分配两个连续的Slot空间。Java语言中明确的64位数据类型只有long和double。
5、局部变量表的Slot可以重用，方法体中定义的变量，其作用于不一定会覆盖整个方法体。
```

