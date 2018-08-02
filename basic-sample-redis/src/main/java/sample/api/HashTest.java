package sample.api;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * @author soyona
 * @Package sample.api
 * @Desc: Hash 用于存储 商品和促销活动的关系
 * 命令：hmget/hmset
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


        Map<String,String> map = new HashMap<String,String>();
        map.put("积分活动","N");
        map.put("包邮活动","Y");
        jedis.hmset(key,map);
        System.out.println("Hash全部元素："+jedis.hgetAll(key));


        System.out.println("Hash获取多个key："+jedis.hmget(key,"包邮活动","积分活动"));;

        RedisUtils.backJedis(jedis);
    }
}
