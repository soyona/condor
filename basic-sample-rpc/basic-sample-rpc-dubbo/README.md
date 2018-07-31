# 1.Dubbo源码分析
## 1.1 并发控制
### 1.1.1 消费端
> [源码：org.apache.dubbo.rpc.filter.ActiveLimitFilter](org.apache.dubbo.rpc.filter.ActiveLimitFilter)
 
> [源码：org.apache.dubbo.rpc.RpcStatus](org.apache.dubbo.rpc.RpcStatus)
```
原子类：AtomicLong，AtomicInteger
并发MAP：
ConcurrentMap<String, RpcStatus> SERVICE_STATISTICS;
ConcurrentMap<String, ConcurrentMap<String, RpcStatus>> METHOD_STATISTICS;
```
```java
到达控制的并发数量，线程wait
```
### 1.1.2 服务端
> [源码：org.apache.dubbo.rpc.filter.ExecuteLimitFilter](org.apache.dubbo.rpc.filter.ExecuteLimitFilter)
 
> [源码：org.apache.dubbo.rpc.RpcStatus](org.apache.dubbo.rpc.RpcStatus)
 
> 信号量控制：
```text
private volatile Semaphore executesLimit;

executesLimit = count.getSemaphore(max);
if(executesLimit != null && !(acquireResult = executesLimit.tryAcquire())) {
    throw new RpcException("Failed to invoke method " + invocation.getMethodName() + " in provider " + url + ", cause: The service using threads greater than <dubbo:service executes=\"" + max + "\" /> limited.");
}
```