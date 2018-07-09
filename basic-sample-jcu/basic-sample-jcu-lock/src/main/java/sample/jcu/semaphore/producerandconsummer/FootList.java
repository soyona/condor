package sample.jcu.semaphore.producerandconsummer;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 *
 * 同时允许10个厨师做菜
 * 同时允许20个就餐者
 *
 * Created by kanglei on 20/10/2017.
 */
public class FootList {
    volatile ReentrantLock lock = new ReentrantLock();
    volatile Condition setCondition = lock.newCondition();
    volatile Condition getCondition = lock.newCondition();

    volatile Semaphore cookSemphore = new Semaphore(10);
    volatile Semaphore dinerSemphore = new Semaphore(20);

    //菜
    volatile String what = "";


    /**
     * 允许同时10个厨师做菜
     */
    public void cook(){
        try {
            cookSemphore.acquire();//此处允许10个厨师同时进来
            lock.lock();//此处限制同时只有 一个厨师做菜
                while(isFull()){//如果有菜
                    //System.out.println(Thread.currentThread().getName()+" 有菜，等待 :");
                    setCondition.await();//等待在 做菜的条件队列
                }
                this.what = "西瓜";//做菜完毕,通知就餐者用餐
                System.out.println(Thread.currentThread().getName()+" 菜已做好。"+this.what);
                getCondition.signalAll();//通知就餐队列getCondition中所有线程（就餐者）
            lock.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            cookSemphore.release();
        }
    }

    /**
     * 允许同时20个就餐者就餐
     * @return
     */
    public String eat(){
        try {
            dinerSemphore.acquire();
                lock.lock();
                while(isEmpty()){
                   // System.out.println(Thread.currentThread().getName()+" 无菜，等待就餐 :");
                    getCondition.await();
                }
                System.out.println(Thread.currentThread().getName()+" 吃菜 :"+ this.what);
                //吃完菜，通知做菜队列的厨师可以做菜了
                this.what = "";
                setCondition.signalAll();
                lock.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            dinerSemphore.release();
        }
        return this.what;
    }


    public boolean isFull(){
        return !what.equals("");
    }

    public boolean isEmpty(){
        return what.equals("");
    }






}
