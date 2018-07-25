package sample.dlock.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;
import sample.dlock.DLock;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author soyona
 * @Package sample.dlock.redis
 * @Desc:
 * @date 2018/7/24 10:39
 */
public class RedisBDLock implements DLock {
    long timeout;
    String LOCK_KEY = "DKEY";
    /**
     *
     * @param key 业务指定 锁资源
     * @param timeout 指定超时时间
     */
    public RedisBDLock(final String key,final long timeout){
        this.LOCK_KEY = key;
        this.timeout = timeout;
    }
    static JedisPool jedisPool ;

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

    //阻塞
    @Override
    public void lock(){
        int i=0;
        while(!tryLock()){
            try {
                ++i;
                Thread.sleep(new Random().nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("会员："+Thread.currentThread().getId()+ " 尝试了 "+i+" 次");
    }
    /**
     * setnx + expireTime 原子性
     * NX 不支持重入
     * 超时：防止获取锁的线程意外终止，导致死锁。
     *
     */
    @Override
    public boolean tryLock() {
        boolean rs = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
//            System.out.println(jedis);
            String result = jedis.set(LOCK_KEY, String.valueOf(Thread.currentThread().getId()), "NX", "PX", 5*1000);
            if ("OK".equals(result)) {
                rs= true;
            }
        }catch (Exception e){
            System.out.println("异常关闭");
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        }finally {
            jedisPool.returnResource(jedis);
        }

        return rs;
    }

    @Override
    public boolean tryLock(long acquireTimeout, TimeUnit unit) {
        final long mtimeout = unit.toMillis(acquireTimeout);
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
                }else{
                    System.out.println("加锁失败！");
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
     * watch事务机制
     */
    @Override
    public void release() {
        Jedis jedis = jedisPool.getResource();
        while(true){
            //如果锁为空，自动释放，循环终止
            if(jedis.get(LOCK_KEY)==null){
                System.out.println("已超时，自动释放");
                break;
            }
            jedis.watch(LOCK_KEY);
            if(String.valueOf(Thread.currentThread().getId()).equals(jedis.get(LOCK_KEY))){
                Transaction transaction = jedis.multi();
                transaction.del(LOCK_KEY);
                List<Object> results = transaction.exec();
                if(results == null){
                    continue;
                }
            }
            jedis.unwatch();
            break;
        }
//        jedis.close();
        jedisPool.returnResource(jedis);
//        System.out.println("释放锁成功");
    }
}
