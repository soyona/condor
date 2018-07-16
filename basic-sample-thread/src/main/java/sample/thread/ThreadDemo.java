package sample.thread;

import org.junit.Test;

/**
 * @author soyona
 * @Package sample.thread
 * @Desc:  Two ways of creating a Thread
 * @date 2018/5/6 19:24
 */
public class ThreadDemo {
    /**
     * By implementing the Runnable interface
     */
    class MyThread implements Runnable{

        @Override
        public void run() {
            System.out.println("implementing  Runnable interface.");
        }
    }
    class MyThread2 extends Thread{
        @Override
        public void run() {
            System.out.println("extending Thread class.");
        }
    }


    /**
     * How to start a thread that implementing the Runnable interface
     */
    @Test
    public void excThread(){
        Thread t = new Thread(new MyThread());
        t.start();
    }

    /**
     * How to start a thread that extending the Thread class
     */
    @Test
    public void excThread2(){
        Thread t = new MyThread2();
        t.start();
    }

}
