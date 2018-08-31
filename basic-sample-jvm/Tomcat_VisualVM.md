# GC分析案例：
> [CMS分析](https://plumbr.io/handbook/garbage-collection-algorithms-implementations/concurrent-mark-and-sweep)
> [GC 调优](https://blog.csdn.net/renfufei/article/details/61924893)
## 配置
```text
JAVA_OPTS="-server -Xms512m -Xmx512m -Xmn200m -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -Xloggc:../logs/gc.log"
```
## 输出详细日志配置：
```text
-XX:+PrintGCDetails
-XX:+PrintGCDateStamps
-XX:+PrintGCTimeStamps
```
> 监控结果：
```text
GC Time:72 collections,891.976ms Last Cause:Allocation Failure
Eden Space(200.000M,200.000M):xxxM,71 collections,809.291ms
Survivor 0(25.000M,25.000M):2.026M
Survivor 1(25.000M,25.000M):0
```
