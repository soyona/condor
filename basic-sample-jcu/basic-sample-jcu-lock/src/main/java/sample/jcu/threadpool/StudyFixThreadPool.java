package sample.jcu.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 考查FixedThreadPool，内部队列采用 LinkedBlockingQueue
 * Created by kanglei on 20/10/2017.
 */
public class StudyFixThreadPool {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);//限定3个大小，
        for (int i = 0; i <10 ; i++) {//三个线程循环执行
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                }
            });
        }

        try {
            TimeUnit.SECONDS.sleep(5);
            executorService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
