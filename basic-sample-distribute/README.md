# 1. CAP理论
> [CAP理论证明原文](http://www.hollischuang.com/archives/666)

>
```text

```

# 2. BASE理论
>
```text

```

>
# 3. 分布式锁
## 3.1 Zookeeper分布式锁
> [Zookeeper分布式锁](../basic-sample-zookeeper/README.md)
## 3.2 Google Chubby分布式锁
> [Google Chubby]()
## 3.3 Redis分布式锁
> [Redis分布式锁](../basic-sample-redis/README.md)
## 3.4 DB分布式锁

# 一致性哈希
## Reference
> http://www.acodersjourney.com/2017/10/system-design-interview-consistent-hashing/
 
> https://www.cnblogs.com/xrq730/p/5186728.html
 
# 一致性哈希JAVA实现

## Dubbo的负载均衡：
> https://blog.csdn.net/Revivedsun/article/details/71022871

## memcached-client:
> https://github.com/dustin/java-memcached-client/blob/master/src/main/java/net/spy/memcached/KetamaNodeLocator.java
 
> https://github.com/dustin/java-memcached-client/blob/master/src/main/java/net/spy/memcached/KetamaIterator.java
## other 
> https://github.com/afghl/hashringdemo/tree/master/src/com/hashringdemo
 
> https://github.com/Jaskey/ConsistentHash/blob/master/src/com/github/jaskey/consistenthash/ConsistentHashRouter.java
 
> https://gist.github.com/bcambel/cd45ab30bd19cb7e69bcb3fb64e71922

# Consistent hashing with bounded loads
> http://ai.googleblog.com/2017/04/consistent-hashing-with-bounded-loads.html

# 10. 业务场景及实现
## 10.1 分布式锁
## 10.2 分布式计数器之"秒杀"
## 10.3 分布式计数器之"会员限购商品数量"
## 10.4 分布式计数器之"会员注册短信验证码一分钟发一条"
> [发短信一分钟限制一条案例](./src/main/java/sample/counter/MsgCounter.java)
## 10.5 分布式计数器之"API限流" 方式一
> [官网](http://redis.io/commands/INCR#pattern-rate-limiter)
 
> [1分钟调用100次](./src/main/java/sample/counter/LimitRequest.java)
```java

```
## 10.6 分布式计数器之"API限流" 方式二

