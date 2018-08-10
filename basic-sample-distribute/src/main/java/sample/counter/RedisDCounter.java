package sample.counter;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import sample.RedisUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author soyona
 * @Package sample.counter
 * @Desc:   incr、incrby、decr、decrby  Atomic OPT
 * @date 2018/7/25 16:06
 */
public class RedisDCounter implements DCounter{
    public String COUNT_KEY;
    public int timeout;
    public RedisDCounter(String key){
        this.COUNT_KEY = key;
        Jedis jedis  = RedisUtils.getJedis();
        jedis.set(COUNT_KEY,"0");
    }

    /**
     *
     * @param key
     * @param timeout sec
     */
    public RedisDCounter(String key, int timeout){
        this.COUNT_KEY = key;
        this.timeout = timeout;
        init(key,timeout);
    }

    private void init(String key, int timeout){
        Jedis jedis  = RedisUtils.getJedis();
        jedis.set(COUNT_KEY,"0");
    }
    @Override
    public long incr() {
        Jedis jedis  = RedisUtils.getJedis();
        if(jedis.get(COUNT_KEY) ==  null){
            init(this.COUNT_KEY,this.timeout);
        }
        long c = jedis.incr(COUNT_KEY);
        if(c == 1){
            jedis.expire(COUNT_KEY,timeout);
        }
        RedisUtils.backJedis(jedis);
        return c;
    }

    @Override
    public long count() {
        Jedis jedis  = RedisUtils.getJedis();
        long c = Long.valueOf(jedis.get(COUNT_KEY));
        return c;
    }
}
