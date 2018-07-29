package sample.counter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.SimpleFormatter;

/**
 * @author soyona
 * @Package sample.counter
 * @Desc: 每一分钟之内一个手机号发送一条短信
 * @date 2018/7/25 17:50
 */
public class MsgCounter {
    static class CThread extends Thread{
        RedisDCounter counter;
        public CThread(RedisDCounter counter){
            this.counter = counter;
        }
        @Override
        public void run() {
            for(;;){
                long l=this.counter.incr();
                if(l>1){
//                    System.out.println(Thread.currentThread().getId()+"禁止发送！");;
                }else{
                    System.out.println(Thread.currentThread().getId()+" 发送短信： 感谢注册会员 "+ printTime());
                }
            }

        }
    }
    public static void main(String[] args) {
        //手机号
        String cell_no = "13876458765";
        String COUNT_PREFFIX =  "MSG_COUNT_";
        RedisDCounter counter = new RedisDCounter(COUNT_PREFFIX+cell_no,5);
        Thread[] ts = new Thread[1];
        for (int i=0;i<ts.length;i++){
            ts[i] = new CThread(counter);
        }
        for (Thread t:ts) {
            t.start();
        }
    }
    private static String printTime(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }
}
