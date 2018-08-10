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
public class StudyInterruptedRunState {






    public static void main(String[] args) {
        //线程 t
        Thread t = new Thread("刘邦"){
            @Override
            public void run() {
                try {
                    for (;;){//死循环。。
                        while(!isInterrupted()){//没有中断时...........
                            System.out.println("执行..");
                        }
                        System.out.println("interrupted..");
                        break;
                    }
                } catch (Exception e) {
                    System.out.println(Thread.currentThread().getName()+" is interrupted.");
                    e.printStackTrace();
                }
            }
        };

        t.start();

        //Main线程睡1秒后再中断 t.interrupt();
        try {
            TimeUnit.SECONDS.sleep(1);
            t.interrupt();//ThreadA线程中断时，抛异常。

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(t.getState()+","+t.isInterrupted());
    }
}
