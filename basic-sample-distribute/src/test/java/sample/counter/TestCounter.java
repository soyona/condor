package sample.counter;

import org.junit.Test;

/**
 * @author soyona
 * @Package sample.counter
 * @Desc:
 * @date 2018/7/25 16:14
 */
public class TestCounter {

   static class CThread extends Thread{
        RedisDCounter counter;
        public CThread(RedisDCounter counter){
            this.counter = counter;
        }
        @Override
        public void run() {
            for(;;){
                long l=this.counter.incr();
                System.out.println(Thread.currentThread().getId()+" -- "+l);;
            }
//            System.out.println(Thread.currentThread().getId()+" -- "+this.counter.count());;
        }
    }


    public static void main(String[] args) {
        RedisDCounter counter = new RedisDCounter("M_COUNT",1);
        Thread[] ts = new Thread[1];
        for (int i=0;i<ts.length;i++){
            ts[i] = new CThread(counter);
        }
        for (Thread t:ts) {
            t.start();
        }
        System.out.println("总数："+counter.count());;
    }
}
