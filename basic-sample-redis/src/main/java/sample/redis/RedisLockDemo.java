package sample.redis;

import redis.clients.jedis.Jedis;

import java.util.Date;

/**
 * @author soyona
 * @Package sample.redis
 * @Desc: 分布式锁实现
 * @date 2018/5/22 11:41
 */
public class RedisLockDemo {
    //本地Redis
    private static String redis_host="localhost";
    private static Jedis jedis;
    private static String lock = "LOCK.SOYONA";
    private static int counter = 0;
    static{
        if(jedis==null){
            //默认端口6379，建立连接
            jedis = new Jedis(redis_host);
            System.out.println(jedis.ping());
        }

    }


    public static void main(String[] args) {
        lock();
        add();
        release();
    }

    /**
     * 计数器增加
     */
    public static void add(){
        counter++;
    }
    /**
     * 获取锁
     */
    public static void lock(){
        jedis.setnx(lock,String.valueOf((new Date()).getTime()));
    }

    /**
     * 释放锁
     */
    public static void release(){
        jedis.del(lock);
    }

}
