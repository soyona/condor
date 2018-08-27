# VisualVM监控 配置：
## 0. 在远程tomcat服务器中开启远程监控，jstatd启动RMI服务
> 在JAVA_HOME/bin下创建文件 "jstatd.all.policy"，内容如下：
```text
grant codebase "file:${java.home}/../lib/tools.jar" {
    permission java.security.AllPermission;
};
```
> 在JAVA_HOME/bin下执行：
```text
./jstatd -J-Djava.rmi.server.hostname=xxx.xxx.xxx.123 -J-Djava.security.policy=jstatd.all.policy -p 55008 &
```

> 在客户端VisualVM中按步骤操作：
>> Remote-> 右键 Add Remote Host..(填写hostname xxx.xxx.xxx.123) 添加 主机
>> 在上步添加好的 主机 右键-> Add jstatd Connection...(添加端口 55008)
>> 自动列出所有JVM容器。。

# 1. 在tomcat中配置:/home/deploy/apache-tomcat-8.0.33-promotion1-8033-9087-8039/bin/
```text
JAVA_OPTS="-server -Xms750m -Xmx750m -XX:PermSize=100m -XX:MaxPermSize=128m -Xss512k -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintHeapAtGC -Xloggc:../logs/gc.log"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.port=12008"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.authenticate=false"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.ssl=false"
```
> 监控结果：
```text
JAVA_OPTS="-server -Xms512m -Xmx512m -Xmn200m -XX:PermSize=100m -XX:MaxPermSize=128m -Xss512k -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintHeapAtGC -Xloggc:../logs/gc.log"
GC Time:72 collections,891.976ms Last Cause:Allocation Failure
Eden Space(200.000M,200.000M):xxxM,71 collections,809.291ms
Survivor 0(25.000M,25.000M):2.026M
Survivor 1(25.000M,25.000M):0
```
