# JDK8配置
```text
JAVA_OPTS="-server -Xms512m -Xmx512m -Xss512k -Xmn200m -XX:NewRatio=1 -XX:MetaspaceSize=50m -XX:MaxMetaspaceSize=80m -XX:+HeapDumpOutOfMemoryError -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -Xloggc:../logs/gc.log"

```
>
```text
-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=../logs/
```
> 配置MetaSpace 最大值
```text
-XX:CompressedClassSpaceSize=300m
```