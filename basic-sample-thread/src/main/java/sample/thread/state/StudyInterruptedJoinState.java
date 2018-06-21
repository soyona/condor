package sample.thread.state;

import java.util.concurrent.TimeUnit;

/**
 * **********************************************
 *          Thread.interrupt()不会中断一个正在运行的线程，
 *          Thread.interrupt()使处于阻塞线程抛异常退出阻塞状态，
 *          Interrupt可以使 调用了 wait/join/sleep 方法的线程中断，sleep/join 抛异常InterruptedException,wait 抛异常IllegalMonitorStateException
 *
 * **********************************************
 *
 *  * Interrupted 中断 waiting 状态的线程
 *  join
 *
 * Created by kanglei on 13/10/2017.
 */
public class StudyInterruptedJoinState {

    static class ThreadA extends Thread{
        Thread joinA = null;
        public ThreadA(Thread t,String name){
            this.setName(name);
            this.joinA = t;
            this.joinA.start();//在构造函数启动线程
        }
        @Override
        public void run() {
            super.run();
            try {
                this.joinA.join();//被线程join，ThreadA线程等待
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName()+" is interrupted.");
                this.joinA.interrupt();
                e.printStackTrace();
            }
        }
    }



    public static void main(String[] args) {
        //线程 t
        Thread t = new Thread("刘邦"){
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (Exception e) {
                    System.out.println(Thread.currentThread().getName()+" is interrupted.");
                    e.printStackTrace();
                }
            }
        };

        //线程 tA
        Thread tA = new ThreadA(t,"项羽");
        tA.start();

        //Main线程睡1秒后再中断 t.interrupt();
        try {
            TimeUnit.SECONDS.sleep(1);
            tA.interrupt();//ThreadA线程中断时，抛异常。

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(t.getState()+","+t.isInterrupted());

        System.out.println(t.getState()+","+t.isInterrupted());

    }
}
