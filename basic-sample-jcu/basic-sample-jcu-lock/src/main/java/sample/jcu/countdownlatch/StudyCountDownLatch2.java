package sample.jcu.countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 *
 * 描述 一个教练等待 10个 跑步者到来的场景，
 *
 * Created by kanglei on 20/10/2017.
 */
public class StudyCountDownLatch2 {

    static class ThreadRunner extends Thread {
        CountDownLatch count = null;
        public ThreadRunner(CountDownLatch count){
            this.count=count;
        }
        @Override
        public void run() {
            count.countDown();
            System.out.println(Thread.currentThread().getName()+"报道。");
        }
    }

    public static void main(String[] args) {
        CountDownLatch count = new CountDownLatch(10);
        ThreadRunner[] runners = new ThreadRunner[10];
        for (int i = 0; i < runners.length; i++) {
            runners[i] = new ThreadRunner(count);
            runners[i].setName("runner"+i);
            runners[i].start();
        }
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"都来了。");
    }

}
