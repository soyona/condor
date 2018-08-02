package sample.api;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author soyona
 * @Package sample.api
 * @Desc:
 * @date 2018/8/2 14:20
 */
public class ZSetTest {
    @Test
    public void testZset(){
        final String key = "productSid_stock_209393";
        Jedis jedis = RedisUtils.getJedis();
        jedis.del(key);
        jedis.zadd(key,2d,"1001");
        jedis.zadd(key,1000d,"1002");
        System.out.println(jedis.zrange(key,0,-1));;
        RedisUtils.backJedis(jedis);
    }
}
