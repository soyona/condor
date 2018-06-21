package sample.thread.state;

import sample.thread.Utils;

/**
 * Created by kanglei on 13/10/2017.
 */
public class StudyStateBlocked {
    final static Object lock = new Object();
    public static void main(String[] args) {
        new Thread(){
            @Override
            public void run() {
                this.setName("kanglei--");
                System.out.println("当前线程ID："+Utils.pid());
                synchronized (lock){
                   try {
                       //持有锁，不释放
                       Thread.sleep(1000L*2000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
            }
        }.start();

        //主线程 sleep 1秒
        try {
            Thread.currentThread().setName("kangleiMain");
            System.out.println("当前线程ID："+ Utils.pid());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //主线程获取锁时，blocked状态
        synchronized (lock){
            try {
                Thread.sleep(300*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

}
