# Minor GC/Major GC/Full GC
```text
Minor GC 针对 Young Generation
Major GC 针对 Old Generation
Full GC 针对 整个堆空间- Young + Old + perm
```
# Stop the world
```text
Minor GC/Major GC/Full GC 都会 STW
```

# 触发Full GC
```text
1. System.gc
2. promotion failed (年代晋升失败,比如eden区的存活对象晋升到S区放不下，又尝试直接晋升到Old区又放不下，那么Promotion Failed,会触发FullGC)

3.CMS的Concurrent-Mode-Failure 由于CMS回收过程中主要分为四步: 
1.CMS initial mark 2.CMS Concurrent mark 3.CMS remark 4.CMS Concurrent sweep。

在2中gc线程与用户线程同时执行，那么用户线程依旧可能同时产生垃圾， 
如果这个垃圾较多无法放入预留的空间就会产生CMS-Mode-Failure， 
切换为SerialOld单线程做mark-sweep-compact。

4.新生代晋升的平均大小 大于 老年代的剩余空间 （为了避免新生代晋升到老年代失败）

当使用G1,CMS 时，FullGC发生的时候 是 Serial+SerialOld。 
当使用ParalOld时，FullGC发生的时候是 ParallNew +ParallOld.

5. 持久代满
```
# 