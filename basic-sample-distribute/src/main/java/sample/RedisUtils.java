package sample;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author soyona
 * @Package sample
 * @Desc:
 * @date 2018/7/25 16:08
 */
public class RedisUtils {
    static JedisPool jedisPool ;
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

    public static Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        }catch (Exception e){
            System.out.println("异常关闭");
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        }finally {
        }
        return jedis;
    }

    public static void backJedis(Jedis jedis){
        if(jedis != null){
            try{
//                System.out.println(Thread.currentThread().getId()+" 释放连接");
                jedisPool.returnResource(jedis);
            }catch (Exception e){
//                System.out.println(Thread.currentThread().getId()+" 释放连接");
//                jedisPool.returnBrokenResource(jedis);
            }finally{
//                jedisPool.returnResource(jedis);
            }

        }
    }
}
