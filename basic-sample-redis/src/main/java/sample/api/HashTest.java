package sample.api;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author soyona
 * @Package sample.api
 * @Desc: Hash 用于存储 商品和促销活动的关系
 * @date 2018/8/2 14:12
 */
public class HashTest {
    @Test
    public void testHash(){
        final String key = "productSid_209393";
        Jedis jedis = RedisUtils.getJedis();
        jedis.del(key);
        jedis.hset(key,"促销活动1","N");
        jedis.hset(key,"促销活动2","Y");
        System.out.println("Hash 元素个数："+jedis.hlen(key));
        System.out.println("Hash全部元素："+jedis.hgetAll(key));
        System.out.println("Hash获取某个key的值："+jedis.hget(key,"促销活动1"));

        System.out.println("Hash删除某个key的值："+jedis.hdel(key,"促销活动1"));
        System.out.println("Hash全部元素："+jedis.hgetAll(key));
        System.out.println("Hash 元素个数："+jedis.hlen(key));

        RedisUtils.backJedis(jedis);
    }
}
