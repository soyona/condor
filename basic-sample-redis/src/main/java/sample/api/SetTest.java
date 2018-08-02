package sample.api;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author soyona
 * @Package sample.api
 * @Desc: 命令：sadd/smembers/sismember/srem/
 * @date 2018/8/2 14:06
 */
public class SetTest {
    @Test
    public void testSet(){
        final String key = "name";
        Jedis jedis = RedisUtils.getJedis();
        jedis.del(key);
        jedis.sadd(key,"soyona","shopin","kakka");
        System.out.println("jedis.smembers全部元素："+jedis.smembers(key));
        System.out.println("jedis.sismember判断存在元素："+jedis.sismember(key,"soyona"));
        System.out.println("jedis.srem删除元素存在元素："+jedis.srem(key,"soyona"));
        System.out.println("jedis.smembers全部元素："+jedis.smembers(key));

        RedisUtils.backJedis(jedis);
    }
}
