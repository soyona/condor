package sample.counter;

import redis.clients.jedis.Jedis;
import sample.RedisUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author soyona
 * @Package sample.counter
 * @Desc: API 每分钟限制调用次数
 * @date 2018/7/29 19:09
 */
public class LimitRequest {
    // 限制API说明
   static String API_URL = "/query/order/";
   // 在时间段内 最大访问次数
   static long MAX_TIMES = 10;
   // 每隔 时间段 ，单位 秒
   static int TIME_GAP = 10;



    public long incr(){
        Jedis jedis  = RedisUtils.getJedis();
        long ts = jedis.incr(API_URL);
        //设置过期时间，在该行， 可能出现程序意外异常，导致API_URL永无过期时间
        if(ts == 1){
            System.out.println("￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥");
            jedis.expire(API_URL,TIME_GAP);
        }
//        System.out.println("第 "+ ts+ "个。");
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
        LimitRequest limitRequest;
        public CThread(LimitRequest limitRequest){
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
        LimitRequest limitRequest= new LimitRequest();
        Thread[] ts = new Thread[100];
        for (int i=0;i<ts.length;i++){
            ts[i] = new CThread(limitRequest);
        }
        for (Thread t:ts) {
            t.start();
        }
    }
}
