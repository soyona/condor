package sample.thread.block;

import java.util.concurrent.TimeUnit;

/**
 * Created by soyona on 19/10/2017.
 */
public class BlockDemo {
    static  Object lock = new Object();
    public static void main(String[] args) {
        Thread t1 = new Thread(){
            @Override
            public void run() {
                System.out.println("进入run....."+this.getState());
                synchronized (lock){
                 System.out.println("已获取锁....."+this.getState());
                }
            }
        };

        Thread t2 = new Thread(){
            @Override
            public void run() {
                System.out.println("进入run.....");
                synchronized (lock){
                    System.out.println("已获取锁.....");
                }
            }
        };
        Thread t3 = new Thread(){
            @Override
            public void run() {
                System.out.println("进入run.....");
                synchronized (lock){
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("已获取锁.....");
                }
            }
        };
        t3.start();
        t1.start();
        t2.start();
        for (;;){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(t1.getName()+"="+t1.getState().toString()+", "+t2.getName()+"="+t2.getState()+", "+t3.getName()+"="+t3.getState());
            if(t1.getState().toString()=="TERMINATED" && t2.getState().toString()=="TERMINATED" && t3.getState().toString()=="TERMINATED" ){
                break;
            }
        }

    }
}
