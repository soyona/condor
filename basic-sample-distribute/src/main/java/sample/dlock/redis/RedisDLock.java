package sample.dlock.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import sample.dlock.DLock;

import java.util.Collections;

/**
 * @author soyona
 * @Package sample.dlock.redis
 * @Desc:
 * @date 2018/7/24 10:28
 */
public class RedisDLock implements DLock{
    static Jedis jedis = new Jedis("127.0.0.1",6379);
    static String LOCK_KEY = "DKEY";
    /**
     * setnx + expireTime 原子性
     * NX 不支持重入
     * 超时：防止获取锁的线程意外终止，导致死锁。
     *
     */
    @Override
    public boolean tryLock() {
        String result = jedis.set(LOCK_KEY, String.valueOf(Thread.currentThread().getId()), "NX", "PX", 5*1000);
        if ("OK".equals(result)) {
            System.out.println("加锁成功"+jedis.get(LOCK_KEY));
            return true;
        }
        return false;
    }

    /**
     * 首先获取锁对应的value值，检查是否与requestId相等，如果相等则删除锁（解锁）。防止删除其他 线程持有
     * 那么为什么要使用Lua语言来实现呢？因为要确保上述操作是原子性的。关于非原子性会带来什么问题，
     * 那么为什么执行eval()方法可以确保原子性，源于Redis的特性，下面是官网对eval命令的部分解释：
     */
    @Override
    public void release() {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(LOCK_KEY), Collections.singletonList(String.valueOf(Thread.currentThread().getId())));

        if (1==Integer.valueOf(result.toString()).intValue()) {
            System.out.println("释放成功");
        }else{
            System.out.println("释放失败");
        }
    }




}
