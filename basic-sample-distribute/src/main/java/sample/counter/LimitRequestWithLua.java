package sample.counter;

import redis.clients.jedis.Jedis;
import sample.RedisUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

/**
 * @author soyona
 * @Package sample.counter
 * @Desc: API 每分钟限制调用次数
 * @date 2018/7/29 19:09
 */
public class LimitRequestWithLua {
    // 限制API说明
   static String API_URL = "/query/order/";
   // 在时间段内 最大访问次数
   static long MAX_TIMES = 10;
   // 每隔 时间段 ，单位 秒
   static int TIME_GAP = 10;


    /**
     *
     String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
     Object result = jedis.eval(script, Collections.singletonList(LOCK_KEY), Collections.singletonList(tid));
        LUA 脚本 原子性
     * @return
     */
    public long incr(){
        Jedis jedis  = RedisUtils.getJedis();
        String script = "local current " +
                "current = redis.call('incr',KEYS[1]) " +
                "if tonumber(current) == 1 then " +
                "    redis.call('expire',KEYS[1],"+TIME_GAP+") " +
                "end"+
                " return tonumber(current)";
        Object result = jedis.eval(script, Collections.singletonList(API_URL), Collections.singletonList(""));

        long ts = Long.valueOf(result.toString());
        RedisUtils.backJedis(jedis);
        return ts;
    }

    /**
     * 是否可访问
     * @return
     */
    public boolean accessable(){
        return incr() > MAX_TIMES?false:true;
    }



    static class CThread extends Thread{
        LimitRequestWithLua limitRequest;
        public CThread(LimitRequestWithLua limitRequest){
            this.limitRequest = limitRequest;
        }
        @Override
        public void run() {
            for(;;){
//                System.out.println("客户端来访：");
                if(this.limitRequest.accessable()){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getId()+" 处理逻辑"+ printTime());
                }else {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("！！！！！！！！！！流量过大，稍后访问。"+printTime());
                }
            }

        }
    }


    private static String printTime(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    public static void main(String[] args) {
        LimitRequestWithLua limitRequest= new LimitRequestWithLua();
        Thread[] ts = new Thread[1];
        for (int i=0;i<ts.length;i++){
            ts[i] = new CThread(limitRequest);
        }
        for (Thread t:ts) {
            t.start();
        }
    }
}
