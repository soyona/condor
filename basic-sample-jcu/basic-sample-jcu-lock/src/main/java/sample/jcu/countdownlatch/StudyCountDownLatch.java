package sample.jcu.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by kanglei on 28/08/2017.
 */
public class StudyCountDownLatch {
    static CountDownLatch count = new CountDownLatch(2);
    public static void main(String[] args) {

        Thread tA = new Thread(){
            @Override
            public void run() {
                this.setName("A");
                System.out.println(Thread.currentThread().getName()+" 进入。");
                try {
                    TimeUnit.SECONDS.sleep(6);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count.countDown();
                System.out.println(Thread.currentThread().getName()+" 执行。");
            }
        };

        Thread tB = new Thread(){
            @Override
            public void run() {
                this.setName("B");
                System.out.println(Thread.currentThread().getName()+" 进入。");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count.countDown();
                System.out.println(Thread.currentThread().getName()+" 执行。");
            }
        };

        /**
         * 该线程阻塞，直到count.countDown()减至0
         */
        Thread tC = new Thread(){
            @Override
            public void run() {
                this.setName("C");
                System.out.println(Thread.currentThread().getName()+" 进入。");
                try {
                    count.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+" 执行。");
            }
        };
        tA.start();
        tB.start();
        tC.start();

        for (;;) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(count.getCount());
            if(count.getCount()==0) break;
        }
    }




}
