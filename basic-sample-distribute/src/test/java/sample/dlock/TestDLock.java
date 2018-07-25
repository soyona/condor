package sample.dlock;

import org.junit.Test;
import sample.dlock.redis.RedisBDLock;
import sample.dlock.redis.RedisDLock;

import java.util.concurrent.TimeUnit;

/**
 * @author soyona
 * @Package sample.dlock
 * @Desc:
 * @date 2018/7/24 10:32
 */
public class TestDLock {

    @Test
    public void testLock(){
        DLock lock = new RedisDLock("PRODUCT_NUM",5*1000);
        lock.lock();
        try {
            System.out.println("执行业务代码....begin");
            //模拟业务执行时间在超时之内 <5*1000
//            Thread.sleep(4*1000);
            //模拟业务执行时间 > 5* 1000
            Thread.sleep(6*1000);
            System.out.println("执行业务代码....end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.release();
    }


    @Test
    public void testBLock(){
        //锁超时时间 默认：5秒
        DLock lock = new RedisBDLock("PRODUCT_NUM",5*1000);
        lock.lock();
        try {
            System.out.println("执行业务代码....begin");
            //模拟业务执行时间在超时之内 <5*1000
            Thread.sleep(4*1000);
            //模拟业务执行时间 > 5* 1000
//            Thread.sleep(6*1000);
            System.out.println("执行业务代码....end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.release();
    }


    /****
     *
     * 模拟 100线程 并发
     *
     */



    //秒杀10个商品
    static  volatile Long  productNum = 10L;
    static class CThread extends Thread{
        DLock lock;
        public CThread(DLock lock){
            this.lock = lock;
        }
        @Override
        public void run() {
            //如果抢购完毕，避免加锁
            if(productNum > 0){
                lock.lock();
                //获取锁之后，判断商品数量
                if(productNum>0){
                    try {
        //                System.out.println("执行业务代码....begin");
                        //模拟业务执行时间在超时之内 <5*1000
                        Thread.sleep(10);
                        System.out.println("会员："+Thread.currentThread().getId()+" 抢到 第"+productNum--+"个商品");
        //                System.out.println("执行业务代码....end");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    System.out.println("会员："+Thread.currentThread().getId()+" 没有抢到商品");
                }
                lock.release();
            }else {
                System.out.println("抢购完毕。");
            }
        }
    }
    @Test
    public void testSecKill(){
        DLock lock = new RedisBDLock("PRODUCT_NUM",5*1000);
        Thread[] ts = new Thread[10];
        for (int i = 0; i < ts.length; i++) {
            ts[i]=new CThread(lock);
        }
        for (Thread t:ts) {
            t.start();
        }
    }


    public static void main(String[] args) {
        DLock lock = new RedisBDLock("PRODUCT_NUM",5*1000);
        Thread[] ts = new Thread[1000];
        for (int i = 0; i < ts.length; i++) {
            ts[i]=new CThread(lock);
        }
        for (Thread t:ts) {
            t.start();
        }
    }
}
