package sample.counter;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import sample.PrintUtils;
import sample.RedisUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author soyona
 * @Package sample.counter
 * @Desc: API 每分钟限制调用次数,采用Redis集合Rpush
 * @date 2018/7/29 19:09
 */
public class LimitRequestWithRpush {
    // 限制API说明
   static String API_URL = "/query/order/";
   // 在时间段内 最大访问次数
   static long MAX_TIMES = 10;
   // 每隔 时间段 ，单位 秒
   static int TIME_GAP = 10;

    public long getCount(){
        Jedis jedis  = RedisUtils.getJedis();
        long l = jedis.llen(API_URL);
        RedisUtils.backJedis(jedis);
        return l;
    }
    /**
     * 是否可访问
     * @return
     */
    public boolean accessable(){
        Jedis jedis  = RedisUtils.getJedis();
        boolean rs = false;
        //如果不存在,初始化时 带有超时时间
        if(!jedis.exists(API_URL)){// 该行 并发 有问题。。。。。。。。。
            Transaction t = jedis.multi();
            t.rpush(API_URL, UUID.randomUUID().toString());
            t.expire(API_URL,TIME_GAP);
            t.exec();
            System.out.println("初始化。");
            rs = true;
        }else{
            long l = jedis.llen(API_URL);
            if(l >= MAX_TIMES){
                rs = false;
            }else{
                jedis.rpush(API_URL, UUID.randomUUID().toString());
                rs = jedis.llen(API_URL) <= MAX_TIMES;
            }
        }
        RedisUtils.backJedis(jedis);
//        System.out.println(jedis.llen(API_URL));
        return rs;
    }



    static class CThread extends Thread{
        LimitRequestWithRpush limitRequest;
        public CThread(LimitRequestWithRpush limitRequest){
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
                    System.out.println(": 处理逻辑"+ PrintUtils.printTime());
                }else {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(":！！！！！！！！！！流量过大，稍后访问。"+PrintUtils.printTime());
                }
            }

        }
    }



    public static void main(String[] args) {
        LimitRequestWithRpush limitRequest= new LimitRequestWithRpush();
        Thread[] ts = new Thread[20];
        for (int i=0;i<ts.length;i++){
            ts[i] = new CThread(limitRequest);
        }
        for (Thread t:ts) {
            t.start();
        }
    }
}
