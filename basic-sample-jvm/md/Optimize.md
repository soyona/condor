# 一、GC监控配置

## IDEA配置一
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
## IDEA配置二
```text
在IDEA->Run->Edit Configuration->选项 Configuration中->VM options: 配置内容如下：

-XX:+PrintGC
-XX:+PrintGCDetails
-XX:+PrintGCDateStamps
-Xloggc:/Users/kanglei/GitHub/condor/basic-sample-jvm/gc.log

```

# 二、GC日志分析
```text
gc.log内容：  

Java HotSpot(TM) 64-Bit Server VM (25.65-b01) for bsd-amd64 JRE (1.8.0_65-b17), built on Oct  6 2015 15:36:46 by "java_re" with gcc 4.2.1 (Based on Apple Inc. build 5658) (LLVM build 2336.11.00)
Memory: 4k page, physical 16777216k(3832408k free)

/proc/meminfo:

CommandLine flags: -XX:InitialHeapSize=268435456 -XX:MaxHeapSize=4294967296 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseParallelGC
2018-06-03T09:33:00.668-0800: 0.123: [GC (System.gc()) [PSYoungGen: 3932K->512K(76288K)] 69468K->66056K(251392K), 0.0006954 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2018-06-03T09:33:00.669-0800: 0.124: [Full GC (System.gc()) [PSYoungGen: 512K->0K(76288K)] [ParOldGen: 65544K->346K(175104K)] 66056K->346K(251392K), [Metaspace: 2639K->2639K(1056768K)], 0.0041394 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
Heap
 PSYoungGen      total 76288K, used 655K [0x000000076ab00000, 0x0000000770000000, 0x00000007c0000000)
  eden space 65536K, 1% used [0x000000076ab00000,0x000000076aba3ee8,0x000000076eb00000)
  from space 10752K, 0% used [0x000000076eb00000,0x000000076eb00000,0x000000076f580000)
  to   space 10752K, 0% used [0x000000076f580000,0x000000076f580000,0x0000000770000000)
 ParOldGen       total 175104K, used 346K [0x00000006c0000000, 0x00000006cab00000, 0x000000076ab00000)
  object space 175104K, 0% used [0x00000006c0000000,0x00000006c0056be0,0x00000006cab00000)
 Metaspace       used 2645K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 287K, capacity 386K, committed 512K, reserved 1048576K
```

# 三、JVM参数配置

## 查看系统的默认的初始化参数：
> [系统内存](https://github.com/soyona/condor/blob/master/basic-sample-linux/free.md)
```text
[kanglei@sj114 bin]$ free
             total       used       free     shared    buffers     cached
Mem:      32827576   30713328    2114248          0      26816    1175908
-/+ buffers/cache:   29510604    3316972
Swap:     16482296    3587180   12895116
```
> JVM参数：java -XX:+PrintFlagsFinal -version | grep -iE 'HeapSize|PermSize|ThreadStackSize'

```text
[kanglei@sj114 bin]$ java -XX:+PrintFlagsFinal -version | grep -iE 'HeapSize|PermSize|ThreadStackSize'
     intx CompilerThreadStackSize                   = 0                                   {pd product}
    uintx ErgoHeapSizeLimit                         = 0                                   {product}
    uintx HeapSizePerGCThread                       = 87241520                            {product}
    uintx InitialHeapSize                          := 526385152                           {product}
    uintx LargePageHeapSizeThreshold                = 134217728                           {product}
    uintx MaxHeapSize                              := 8405385216                          {product}
     intx ThreadStackSize                           = 1024                                {pd product}
     intx VMThreadStackSize                         = 1024                                {pd product}
java version "1.8.0_73"
Java(TM) SE Runtime Environment (build 1.8.0_73-b02)
Java HotSpot(TM) 64-Bit Server VM (build 25.73-b02, mixed mode)
```
> 分析解释：
```text
Java heap size
InitialHeapSize = 526385152 bytes (502M) and MaxHeapSize = 8405385216 bytes (8016M).
```
```text 
PermGen Size
PermSize = 21757952 bytes (20.75M), MaxPermSize = 174063616 bytes (166M)
```
```text
Thread Stack Size
ThreadStackSize = 1024 kilobytes (1M)
```

## 配置：线程栈大小
```text
-Xss128K：每个线程的堆栈大小为128K。
 
JDK5.0以后每个线程堆栈大小为1M，以前每个线程堆栈大小为256K。
 
更具应用的线程所需内存大小进行调整。在相同物理内存下，减小这个值能生成更多的线程。
 
但是操作系统对一个进程内的线程数还是有限制的，不能无限生成，经验值在3000~5000左右。
```

## 配置：堆大小
> JDK8默认
```text
Heap sizes
Initial heap size of 1/64 of physical memory up to 1Gbyte
Maximum heap size of 1/4 of physical memory up to 1Gbyte
以上根据不同VM有不同值

例如：物理内存 8096M=8G
初始化大小：8096 * 1/64
最大值：8096 * 1/4
```
```text
-Xms2g：JVM启动初始化堆大小为2g，Xms的默认是物理内存的1/64但小于1G。
-Xmx2g：JVM最大的堆大小为2g，Xmx默认是物理内存的1/4但小于1G；将-Xms和-Xmx的值配置为一样，可以避免每次垃圾回收完成后对JVM堆大小进行重新的调整。
```
## 配置：新生代大小
```text
-Xmn512M：新生代大小为512M
```
## 配置：新生代和老年代的比例
> java -XX:+PrintFlagsFinal -version | grep NewRatio
```text
默认是-XX:NewRatio=2，新生代和老年代的大小比例为1：2
```
```text
-XX:NewRatio=4：JVM堆的新生代和老年代的大小比例为1：4
```
## 配置：Surivor与Eden比例
> 默认配置：java -XX:+PrintFlagsFinal -version | grep SurvivorRatio
```text
[deploy@promotion105 bin]$ java -XX:+PrintFlagsFinal -version| grep SurvivorRatio
    uintx InitialSurvivorRatio                      = 8                                   {product}
    uintx MinSurvivorRatio                          = 3                                   {product}
    uintx SurvivorRatio                             = 8                                   {product}
    uintx TargetSurvivorRatio                       = 50                                  {product}
java version "1.8.0_73"
Java(TM) SE Runtime Environment (build 1.8.0_73-b02)
Java HotSpot(TM) 64-Bit Server VM (build 25.73-b02, mixed mode)
```

```text
-XX:SurvivorRatio=4：
新生代一个Surivor区和Eden区的比例为1：4
那么，新生代有2个Surivor区与Eden区的比例是 2：4
也就是说 一个Survivor占据整个新生代的比例是 1/6

```
## 配置堆 压缩与扩展
> 默认配置： java -XX:+PrintFlagsFinal -version | grep HeapFree
```text
[deploy@promotion105 ~]$ java -XX:+PrintFlagsFinal -version | grep HeapFree
    uintx GCHeapFreeLimit                           = 2                                   {product}
    uintx MaxHeapFreeRatio                          = 100                                 {manageable}
    uintx MinHeapFreeRatio                          = 0                                   {manageable}
java version "1.8.0_73"
Java(TM) SE Runtime Environment (build 1.8.0_73-b02)
Java HotSpot(TM) 64-Bit Server VM (build 25.73-b02, mixed mode)
```
##### 当-Xmx 和-Xms 相等时，-XX:MinHeapFreeRatio 和-XX:MaxHeapFreeRatio 两个参数无效。
```text
当空闲空间比率 小于MinHeapFreeRatio时 扩展
当空闲空间比率 大于MaxHeapFreeRatio时 缩小
```
## 配置：对象进入老年代的年龄
> 默认配置：java -XX:+PrintFlagsFinal -version | grep MaxTenuringThreshold
```text
[deploy@promotion105 bin]$ java -XX:+PrintFlagsFinal -version | grep MaxTenuringThreshold
    uintx MaxTenuringThreshold                      = 15                                  {product}
java version "1.8.0_73"
Java(TM) SE Runtime Environment (build 1.8.0_73-b02)
Java HotSpot(TM) 64-Bit Server VM (build 25.73-b02, mixed mode)
```
```text
-XX:MaxTenuringThreshold=1：新生代的对象经过几次垃圾回收后（如果还存活），进入老年代。如果该参数设置为0，这表示新生代的对象在垃圾回收后，不进入survivor区，直接进入老年代
```
## 配置：元空间
> 默认配置：java -XX:+PrintFlagsFinal -version | grep MetaspaceSize
```text
[deploy@promotion105 bin]$ java -XX:+PrintFlagsFinal -version | grep MetaspaceSize
    uintx InitialBootClassLoaderMetaspaceSize       = 4194304                             {product}
    uintx MaxMetaspaceSize                          = 18446744073709547520                    {product}
    uintx MetaspaceSize                             = 21807104                            {pd product}
java version "1.8.0_73"
Java(TM) SE Runtime Environment (build 1.8.0_73-b02)
Java HotSpot(TM) 64-Bit Server VM (build 25.73-b02, mixed mode)
```

> JDK7 叫持久代
```text
-XX:PermSize=128M：JVM持久代的初始化大小为128M
-XX:MaxPermSize=128M：JVM持久代的最大大小为128M
 
持久代一般固定大小为64m，所以增大年轻代后，将会减小年老代大小。
此值对系统性能影响较大，Sun官方推荐配置为整个堆的3/8。
设置非堆内存初始值，默认是物理内存的1/64；
由XX:MaxPermSize设置最大非堆内存的大小，默认是物理内存的1/4
```
> JDK8
>> 配置 元数据区大小：
```text
-XX:MetaspaceSize=N
-XX:MaxMetaspaceSize=N//到达此值 会引起Full GC
```
```text
通过 java -XX:+PrintFlagsFinal 查看
MetaspaceSize: 默认大小为 20M

[kanglei@sj114 logs]$ java -XX:+PrintFlagsFinal|grep Meta
    uintx InitialBootClassLoaderMetaspaceSize       = 4194304                             {product}
    uintx MaxMetaspaceExpansion                     = 5451776                             {product}
    uintx MaxMetaspaceFreeRatio                     = 70                                  {product}
    uintx MaxMetaspaceSize                          = 18446744073709547520                    {product}
    uintx MetaspaceSize                             = 21807104                            {pd product}
    uintx MinMetaspaceExpansion                     = 339968                              {product}
    uintx MinMetaspaceFreeRatio                     = 40                                  {product}
     bool TraceMetadataHumongousAllocation          = false                               {product}
     bool UseLargePagesInMetaspace                  = false                               {product}
```
### 配置 元空间 空闲比
>
> 增长和释放比例：
```text
-XX:MinMetaspaceFreeRatio=40 //不断增加，在到达设定的Max之前所经历的GC次数也就越少 
-XX:MaxMetasaceFreeRatio=70 
空闲空间的比大于70%时，释放Metaspace的部分空间
空闲比小于这个40%时，增长Metaspace的大小
```
> 增长幅度：
```text
-XX:MaxMetaspaceExpansion=5451776
-XX:MinMetaspaceExpansion=339968
增长幅度： 5M 与 332KB 之间
```

### 
> -XX:CompressedClassSpaceSize=300m
```text
查看默认值：1G
[kanglei@sj114 ~]$ java -XX:+PrintFlagsFinal -version|grep CompressedClassSpaceSize
    uintx CompressedClassSpaceSize                  = 1073741824                          {product}
java version "1.8.0_73"
Java(TM) SE Runtime Environment (build 1.8.0_73-b02)
Java HotSpot(TM) 64-Bit Server VM (build 25.73-b02, mixed mode)
```

### 优化元空间
> [参考](https://www.cnblogs.com/duanxz/p/3520829.html)
## 配置：垃圾回收器
> 参考：https://mp.weixin.qq.com/s/bOarreWhQJmS6VTZfFcsZw
### 并行收集器
```text
-XX:+UseParallelGC：使用并行的垃圾收集器，但仅针对新生代有效，老年代仍然使用串行收集器
-XX:ParallelGCThread=4：设置并行垃圾回收器的线程为4个，该设置最好与处理器的数目相同
-XX:+UseParalleOldGC：配置老年代使用并行垃圾收集器，JDK1.6支持老年代使用并行收集器
-XX:MaxGCPauseMillis=100：设置每次新生代每次收集器垃圾回收的最长时间，如果无法满足该时间，JVM会自动调整新生代区的大小，以满足该值
-XX:+UseAdaptiveSizePolicy：设置此值后，JVM会自动调整新生代大小以及相应的surivor区的比例，以达到设置的最低响应时间或者收集频率等
```

### 并发收集器
>-XX:UseConcMarkSweepGC
```text
设置JVM堆的老年代使用CMS并发收集器，设置该参数后，-XX:NewRatio参数失效，但-Xmn参数依然有效
 
CMS收集器也被称为短暂停顿并发收集器。它是对年老代进行垃圾收集的。
CMS收集器通过多线程并发进行垃圾回收，尽量减少垃圾收集造成的停顿。
CMS收集器对年轻代进行垃圾回收使用的算法和Parallel收集器一样。
这个垃圾收集器适用于不能忍受长时间停顿要求快速响应的应用。
```
>-XX:UseParNewGC：

```text
设置新生代使用并发收集器，在JDK1.5以上，JVM会根据系统自动设置
 
对年轻代采用多线程并行回收，这样收得快；
```
>-XX:+CMSClassUnloadingEnabled
```text
 
如果你启用了CMSClassUnloadingEnabled ，垃圾回收会清理持久代，移除不再使用的classes。
这个参数只有在 UseConcMarkSweepGC  也启用的情况下才有用。
```

> -XX:+DisableExplicitGC
```text
禁止System.gc()，免得程序员误调用gc方法影响性能；
```

>-XX:+UseCMSInitiatingOccupancyOnly
```text
标志来命令JVM不基于运行时收集的数据来启动CMS垃圾收集周期。而是，当该标志被开启时，JVM通过CMSInitiatingOccupancyFraction的值进行每一次CMS收集，而不仅仅是第一次。然而，请记住大多数情况下，JVM比我们自己能作出更好的垃圾收集决策。因此，只有当我们充足的理由(比如测试)并且对应用程序产生的对象的生命周期有深刻的认知时，才应该使用该标志。
```
>-XX:CMSInitiatingOccupancyFraction=68
```text
默认CMS是在tenured generation(年老代）占满68%的时候开始进行CMS收集，如果你的年老代增长不是那么快，并且希望降低CMS次数的话，可以适当调高此值；
```
> -XX:CMSFullGCsBeforeCompaction=5
```text

设置5才CMSGC后对堆空间进行压缩、整理

```
>-XX:+UseCMSCompactAtFullCollection
```text
打开对老年代的压缩，可能会影响性能，但可以消除堆碎片
```

>-XX:+HeapDumpOnOutOfMemoryError
```text
此参数可以控制OutOfMemoryError时打印堆的信息
```
# 三、监控工具
# 四、JVM调优目标
> [参考：JVM调优目标](https://www.ibm.com/developerworks/cn/java/j-lo-jvm-optimize-experience/index.html)
### 4.1 
- 减少堆内存震荡，减少GC次数：设置-Xms/-Xmx 相等
- 减少线程栈的大小，可以节省系统内存，从而增加线程数量：-Xss  
- 增加年轻的大小 -Xmn2g，减少YGC次数
- 年轻代使用并行垃圾收集器，可以减少GC时间：–XX:+UseParallelGC
- 设置回收垃圾的线程数量，可以设置与CPU数量相等。–XX:ParallelGC-Threads
- 老年代设置并行垃圾收集器。–XX:+elOldGC

### 4.2 尝试使用大的内存分页
> 什么是MMU？[参考：现代操作系统P109~P110]()
```text
MMU: Memory management Unit，内存管理单元，在现代计算机系统中，利用虚拟地址替代物理地址，MMU负责将虚拟地址映射到物理地址；
虚拟地址不是被直接送到内存总线，而是先送到MMU单元，由MMU单元映射到物理地址。
```
### 4.3 使用非占有的垃圾回收器
```text
为了减少STW时间，首先关注CMS回收器
为了减少FullGC次数，应该尽量让对象预留在年轻代（MinorGC成本远小于FullGC）
```
>> 如此配置：
```text
java 
 –Xmx3550m //
 –Xms3550m //避免堆内存震荡，减少MinorGC
 –Xmn2g  //新生代大小
 –Xss128k //线程栈大小
 –XX:ParallelGCThreads=20 //20个线程并行回收
 –XX:+UseConcMarkSweepGC //使用CMS
 –XX:+UseParNewGC //年轻代使用并行回收器
 –XX:+SurvivorRatio=8 // 8:1:1
 –XX:TargetSurvivorRatio=90 //设置 Survivor 区的可使用率。这里设置为 90%，则允许 90%的 Survivor 空间被使用。默认值是 50%。故该设置提高了 Survivor 区的使用率。当存放的对象超过这个百分比，则对象会向年老代压缩。因此，这个选项更有助于将对象留在年轻代。
 –XX:MaxTenuringThreshold=31 //晋升年龄（MinorGC次数）超过该年龄 进入 年老代，年龄越大，待在年轻代时间越长
```
>> 分析
```text

```