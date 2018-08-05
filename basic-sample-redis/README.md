# Redis设计原理
# Redis一致性哈希原理
> [Redis一致性哈希原理](../basic-sample-distribute/README.md)
# Redis实现分布式锁

## setnx 实现分布式锁
-   语法：SETNX name value
-   SETNX 是『SET if Not eXists』(如果不存在，则 SET)的简写
-   描述：将 key 的值设为 value ，当且仅当 key 不存在；若给定的 key 已经存在，则 SETNX 不做任何动作
-   时间复杂度：O(1)
-   设置成功，返回 1；设置失败，返回 0 
```text
String result = jedis.set(LOCK_KEY, String.valueOf(Thread.currentThread().getId()), "NX", "PX", 5*1000);
if ("OK".equals(result)) {
    rs= true;
}
```
## setnx实现分布式锁的死锁问题
-   如果一个进程获取锁后，由于该进程异常、程序崩溃导致该锁无法被释放，其他进程永远等待。
-   死锁参考：https://www.cnblogs.com/onions/p/5685674.html
### 参考
-   http://doc.redisfans.com/string/setnx.html
-   http://doc.redisfans.com/

## list分布式队列
> [代码实例](./src/main/java/sample/queue/QueueTest.java)
 
> 实现原理： 
```text
lpush/rpop
```