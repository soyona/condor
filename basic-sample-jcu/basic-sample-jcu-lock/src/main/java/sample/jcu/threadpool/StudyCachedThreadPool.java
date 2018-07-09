package sample.jcu.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * 考查线程池的创建，内部队列采用 SynchronousQueue
 *
 * Created by kanglei on 20/10/2017.
 */
public class StudyCachedThreadPool {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i <10 ; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        //System.out.println(System.currentTimeMillis()+" "+ Thread.currentThread().getName()+" End.");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i <10 ; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        //System.out.println(System.currentTimeMillis()+" "+ Thread.currentThread().getName()+" End.");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        try {
            TimeUnit.SECONDS.sleep(2);
            executorService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
