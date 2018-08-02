package sample.api;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author soyona
 * @Package sample.api
 * @Desc: 命令：sadd/smembers/sismember/srem/scard/sdiff/sunion
 * @date 2018/8/2 14:06
 */
public class SetTest {
    @Test
    public void testSet(){
        final String key = "name";
        final String keyB = "company";
        Jedis jedis = RedisUtils.getJedis();
        jedis.del(key);
        jedis.sadd(key,"soyona","shopin","kakka");
        System.out.println("jedis.smembers全部元素："+jedis.smembers(key));
        System.out.println("jedis.sismember判断存在元素："+jedis.sismember(key,"soyona"));
        System.out.println("jedis.srem删除元素存在元素："+jedis.srem(key,"soyona"));
        System.out.println("jedis.smembers全部元素："+jedis.smembers(key));
        System.out.println("jedis.scard元素数量："+jedis.scard(key));

        jedis.sadd(keyB,"Xiaomi","shopin","SAP");

        //差集：返回存在第一个集合，但不在第二个集合的元素
        System.out.println("第一个集合："+jedis.smembers(key));
        System.out.println("第二个集合："+jedis.smembers(keyB));
        System.out.println("差集："+jedis.sdiff(key,keyB));;

        //并集
        System.out.println("并集："+jedis.sunion(key,keyB));

        RedisUtils.backJedis(jedis);
    }
}
