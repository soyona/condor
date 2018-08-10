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
 * Created by kanglei on 13/10/2017.
 */
public class StudyInterruptedWaitState {

    public static void main(String[] args) {
        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    wait(5);//当前线程睡两秒,在5秒之内被中断，在此处抛异常，
                    while(true){
                        System.out.println("进入 while 循环.");
                    }
                } catch (IllegalMonitorStateException e) {
                    System.out.println("$$$$$");
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    System.out.println("####");
                    e.printStackTrace();
                }
            }
        };
        t.start();

        try {
            TimeUnit.SECONDS.sleep(1);//Main线程睡1秒，等待t线程进入睡眠状态
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(t.getState()+","+t.isInterrupted());
        t.interrupt();//中断处于WAITING状态的t线程
        System.out.println(t.getState()+","+t.isInterrupted());
    }
}
