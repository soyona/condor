package sample.jcu.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by kanglei on 20/10/2017.
 */
public class StudyThreadPoolExecutor {
    public static void main(String[] args) {
        BlockingQueue queue = new LinkedBlockingQueue<Runnable>();

        /**
         * corePoolSize  定义：核心线程数
         * maximumPoolSize: 第一最大的线程数
         * keepAliveTime：超过corePoolSize数量的线程，等待多久会被删除
         */
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,3,5, TimeUnit.SECONDS,queue);
        System.out.println(threadPoolExecutor.getActiveCount());
        System.out.println(threadPoolExecutor.getPoolSize());
        System.out.println(threadPoolExecutor.getMaximumPoolSize());


        for (int i = 0; i <40 ; i++) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName());
                }
            });
        }

        for (;;){
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("queue: "+queue.size()+","+threadPoolExecutor.getActiveCount());
                if(queue.size()==0){
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
