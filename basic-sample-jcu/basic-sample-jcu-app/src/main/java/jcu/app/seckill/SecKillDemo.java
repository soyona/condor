package jcu.app.seckill;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author soyona
 * @Package sample.jcu.countdownlatch
 * @Description:
 * @date 2016/5/23 22:21
 */
public class SecKillDemo {
    //并发线程数
    private static int thread_num = 2000;

    //控制同时并发
    private static CountDownLatch countDownLatch = new CountDownLatch(thread_num);


    public static  final int total=50;
    //抢购商品数量
    private static int product_num=50;

    //抢购结果记录
    private static ConcurrentHashMap result = new ConcurrentHashMap();
    /**
     * @author soyona
     * @desc 初始化线程
     * @date: 2015/5/23 22:24
     * @param:
     * @return:
     */
    public static void init() throws InterruptedException {
        List<Thread> threads = new ArrayList<Thread>(thread_num);
        //循环创建线程
        for (int i = 0; i < thread_num ; i++) {
           Thread t = new Thread(){
                @Override
                public void run() {
                    try {
                        //线程start后 等待
                        countDownLatch.await();
                        //countDownLatch count达到线程数 开始执行
                        handle();
                        
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
           threads.add(t);
            t.start();
            countDownLatch.countDown();
            //***注意 join的位置，会造成死等
//          t.join();
        }

        for (Thread t:threads) {
            t.join();
        }
        System.out.println("抢购完毕！");
    }

    /**
     * @author soyona
     * @date: 2015/5/23  22:31
     * @param: []
     * @return: void
     */
    public static void handle(){
        if(product_num>0){
            int pro_no=product_num--;
            System.out.println("线程ID："+Thread.currentThread().getId()+" 抢到第"+pro_no+"个");
            result.put(Thread.currentThread().getId(),pro_no);
        }
    }


    public static void main(String[] args) throws InterruptedException {
        init();
        //抢购结果：
        if(result.size()>total){
            System.out.println("发生超卖！");
            System.out.println(result.toString());
        }
    }

}
