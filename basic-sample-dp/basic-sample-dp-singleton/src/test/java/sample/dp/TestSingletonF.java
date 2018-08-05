package sample.dp;

import org.junit.Test;
import sample.dp.singleton.SingletonF;

import java.util.concurrent.CountDownLatch;

/**
 * @author soyona
 * @Package sample.dp
 * @Desc:
 * @date 2018/8/5 16:08
 */
public class TestSingletonF {
    final int  num = 1000;
    final CountDownLatch countDownLatch = new CountDownLatch(num);
    @Test
    public void testSingtonWithCAS(){
        System.out.println(SingletonF.getInstance());
    }



    @Test
    public void testSingtonWithCASConcurrency(){
        Thread[] threads = new Thread[num];
        //初始化10个线程
        for(int i=0;i < num;i++){
            Thread  t = new Thread(){
                @Override
                public void run() {
                    try {
                        //等待，直到coutDownLatch减少 为0
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //执行业务
                    System.out.println(SingletonF.getInstance());
                }
            };
            threads[i] = t;
            t.start();
            //线程开启后
            countDownLatch.countDown();
        }
        //启动所有线程
    }
}
