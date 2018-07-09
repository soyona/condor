package sample.jcu.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Cyclic比CountDownLatch功能强大,
 * 参数parties，满足后，会重置为0
 * 如果线程数 大于 参数parties，parties会自动重置，继续等待parties满足后执行，runnable方法；
 *
 *
 * Created by kanglei on 28/08/2017.
 */
public class StudyCyclicBarrier {
    static class ThreadRunner extends Thread {
        CyclicBarrier cb=null;
        public ThreadRunner(CyclicBarrier cb){
            this.cb = cb;
        }
        @Override
        public void run() {
            try {
//                TimeUnit.SECONDS.sleep(2);
                System.out.println(Thread.currentThread().getName()+" 到了。");
                this.cb.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        final CyclicBarrier cb=new CyclicBarrier(2, new Runnable() {
            @Override
            public void run() {
                System.out.println("OK,都到齐了,我们准备下一步");
            }
        });  //三个线程同时到达
        ThreadRunner[] runners = new ThreadRunner[13];
        for (int i = 0; i < runners.length; i++) {
            runners[i] = new ThreadRunner(cb);
            runners[i].setName("运动员-"+i);
            runners[i].start();
        }

    }
}
