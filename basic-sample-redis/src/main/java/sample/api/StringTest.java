package sample.api;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author soyona
 * @Package sample.api
 * @Desc:
 * @date 2018/8/2 11:20
 */
public class StringTest {
    @Test
    public void testString(){
        Jedis jedis = RedisUtils.getJedis();
        jedis.set("name","soyona");
        String v = jedis.get("name");
        System.out.println(v);
        RedisUtils.backJedis(jedis);
    }
}
