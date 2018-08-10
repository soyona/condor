package sample.api;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author soyona
 * @Package sample.api
 * @Desc:
 * 业务应用-数据字典：
 *
 * 支付方式列表：
 * 门店列表：
 * 状态列表：
 * 品牌列表：
 *
 * @date 2018/8/2 11:22
 */
public class ListTest {
    @Test
    public void testList(){
        final String key = "name";
        Jedis jedis = RedisUtils.getJedis();
        jedis.del(key);
        jedis.lpush(key,"soyona","shopin","kakka");
        for (int i = 0; i < jedis.llen(key); i++) {
            System.out.println("遍历："+jedis.lindex(key,i));
        }
        System.out.println("全部元素："+jedis.lrange(key,0,-1));
        System.out.println(jedis.llen(key));
        System.out.println("移除列表 左头："+jedis.lpop(key));
        System.out.println(jedis.lpop(key));


        RedisUtils.backJedis(jedis);
    }



    @Test
    public void testSort(){
        final String key = "age";
        Jedis jedis = RedisUtils.getJedis();
        jedis.del(key);
        jedis.lpush(key,"100","3","98");
        System.out.println("All:"+jedis.lrange(key,0,-1));
        System.out.println(jedis.sort(key));
    }
}
