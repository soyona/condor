package sample.dlock.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import sample.dlock.DLock;

import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author soyona
 * @Package sample.dlock.redis
 * @Desc:
 * @date 2018/7/24 10:28
 */
public class RedisDLock implements DLock{
    static JedisPool jedisPool ;
    String LOCK_KEY;
    long timeout;
    //保存分布式系统中全局线程唯一标识，避免 在方法间传递
    static final ThreadLocal<String> threadLocal = new ThreadLocal<String>();
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        config.setMaxTotal(100);
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(5);
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；单位毫秒
        //小于零:阻塞不确定的时间,  默认-1
        config.setMaxWaitMillis(1000*100);
        //在borrow(引入)一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setTestOnBorrow(true);
        //return 一个jedis实例给pool时，是否检查连接可用性（ping()）
        config.setTestOnReturn(true);
        //connectionTimeout 连接超时（默认2000ms）
        //soTimeout 响应超时（默认2000ms）
        jedisPool = new JedisPool(config, "127.0.0.1", 6379,  0, null);
    }

    /**
     *
     * @param key 业务指定 锁资源
     * @param timeout 指定超时时间
     */
    public RedisDLock(final String key,final long timeout){
        this.LOCK_KEY = key;
        this.timeout = timeout;
    }

    //阻塞
    @Override
    public void lock(){
        while(!tryLock()){
            try {
                Thread.sleep(new Random().nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * setnx + expireTime 原子性
     * NX 不支持重入
     * 超时：超时自动释放，防止获取锁的线程意外终止，导致死锁。
     *
     */
    @Override
    public boolean tryLock() {
        Jedis jedis = null;
        jedis = jedisPool.getResource();
        String result = jedis.set(LOCK_KEY, threadIDhold(), "NX", "PX", this.timeout);
        if ("OK".equals(result)) {
            System.out.println("加锁成功"+jedis.get(LOCK_KEY));
            return true;
        }
        return false;
    }

    @Override
    public boolean tryLock(long timeout, TimeUnit unit) {
        final long mtimeout = unit.toMillis(timeout);
        final long deadline = System.currentTimeMillis() + mtimeout;
        Jedis jedis = null;
        try{

            jedis = jedisPool.getResource();
            for(;;){
                //如果等待超时
                if((deadline - System.currentTimeMillis()) < 0){
                    return false;
                }
                String result = jedis.set(LOCK_KEY, threadIDhold(), "NX", "PX", this.timeout);
                if ("OK".equals(result)) {
                    System.out.println("加锁成功"+jedis.get(LOCK_KEY));
                    return true;
                }
            }
        }catch (Exception e){
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        }finally {
            jedisPool.returnResource(jedis);
            return false;
        }
    }

    private String threadIDhold(){
        String  tid = UUID.randomUUID().toString();
        threadLocal.set(tid);
        return tid;
    }

    /**
     * 首先获取锁对应的value值，检查是否与TID相等，如果相等则删除锁（解锁）。防止删除其他 线程持有
     * 那么为什么要使用Lua语言来实现呢？因为要确保上述操作是原子性的。关于非原子性会带来什么问题，
     * 那么为什么执行eval()方法可以确保原子性，源于Redis的特性，下面是官网对eval命令的部分解释：
     */
    @Override
    public void release() {
        Jedis jedis = jedisPool.getResource();
        String tid = threadLocal.get();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(LOCK_KEY), Collections.singletonList(tid));

        if (1==Integer.valueOf(result.toString()).intValue()) {
            System.out.println("释放成功");
        }else{
            System.out.println("释放失败");
        }
    }





}
