package sample.jcu.threadpool;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author soyona
 * @Package sample.jcu.threadpool
 * @Desc:
 * @date 2018/8/14 21:42
 */
public class StudyScheduledThreadPoolExecutor {

   static class Mytask extends TimerTask{
        @Override
        public void run() {
            System.out.println("d");
        }
    }


    public static void main(String[] args) {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);//启用2个线程
        TimerTask t1 = new Mytask();
        pool.scheduleAtFixedRate(t1,0,2, TimeUnit.SECONDS);
    }
}
