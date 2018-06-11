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

## 配置：线程栈大小
```text
-Xss128K：每个线程的堆栈大小为128K
```

## 配置：堆大小
```text
-Xms2g：JVM启动初始化堆大小为2g，Xms的默认是物理内存的1/64但小于1G。
-Xmx2g：JVM最大的堆大小为2g，Xmx默认是物理内存的1/4但小于1G；将-Xms和-Xmx的值配置为一样，可以避免每次垃圾回收完成后对JVM堆大小进行重新的调整。
-Xmn512M：堆中的新生代大小为512M
```
## 配置：持久代大小

```text
-XX:PermSize=128M：JVM持久代的初始化大小为128M
-XX:MaxPermSize=128M：JVM持久代的最大大小为128M
```
## 配置：新生代和老年代的比例

```text
-XX:NewRatio=4：JVM堆的新生代和老年代的大小比例为1：4
```
## 配置：Surivor与Eden比例

```text
-XX:SurvivorRatio=4：新生代Surivor区（新生代有2个Surivor区）和Eden区的比例为2：4

```
## 配置：对象进入老年代的年龄

```text
-XX:MaxTenuringThreshold=1：新生代的对象经过几次垃圾回收后（如果还存活），进入老年代。如果该参数设置为0，这表示新生代的对象在垃圾回收后，不进入survivor区，直接进入老年代
```
## 配置：垃圾回收器

```text
-XX:+UseParallelGC：使用并行的垃圾收集器，但仅针对新生代有效，老年代仍然使用串行收集器
-XX:ParallelGCThread=4：设置并行垃圾回收器的线程为4个，该设置最好与处理器的数目相同
-XX:+UseParalleOldGC：配置老年代使用并行垃圾收集器，JDK1.6支持老年代使用并行收集器
-XX:MaxGCPauseMillis=100：设置每次新生代每次收集器垃圾回收的最长时间，如果无法满足该时间，JVM会自动调整新生代区的大小，以满足该值
-XX:+UseAdaptiveSizePolicy：设置此值后，JVM会自动调整新生代大小以及相应的surivor区的比例，以达到设置的最低响应时间或者收集频率等

-XX:UseConcMarkSweepGC：设置JVM堆的老年代使用CMS并发收集器，设置该参数后，-XX:NewRatio参数失效，但-Xmn参数依然有效
-XX:UseParNewGC：设置新生代使用并发收集器，在JDK1.5以上，JVM会根据系统自动设置
-XX:CMSFullGCsBeforeCompaction=5：设置5才CMSGC后对堆空间进行压缩、整理
-XX:+UseCMSCompactAtFullCollection：打开对老年代的压缩，可能会影响性能，但可以消除堆碎片
```

# 三、监控工具
