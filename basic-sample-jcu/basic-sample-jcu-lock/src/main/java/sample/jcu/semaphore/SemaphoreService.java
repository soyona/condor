package sample.jcu.semaphore;

import java.util.concurrent.Semaphore;

/**
 * Created by kanglei on 20/10/2017.
 */
public class SemaphoreService {
    Semaphore semaphore = new Semaphore(2);

    public void test(){
        try {
            semaphore.acquire();
            for (int i = 0; i <50 ; i++) {
                System.out.println(Thread.currentThread().getName()+"  @  "+i);
            }
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
