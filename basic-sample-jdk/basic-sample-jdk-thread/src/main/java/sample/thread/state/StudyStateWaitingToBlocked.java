package sample.thread.state;

import java.util.concurrent.TimeUnit;

/**
 * Created by kanglei on 13/10/2017.
 */
public class StudyStateWaitingToBlocked {
    final static Object lock = new Object();

    public static void main(String[] args) {
        /**
         * A 线程首先获取lock对象锁，sleep 10秒，状态为time waiting，此时B线程状态 blocked
         * 10秒后，A线程执行wait放弃锁，状态为Blocked，B线程获取锁执行sleep  ，状态为 time waiting
         *
         *
         *
         *
         * "B" prio=5 tid=0x00007fd425823000 nid=0x5703 waiting on condition [0x00007000024ba000]
         java.lang.Thread.State: TIMED_WAITING (sleeping)
         at java.lang.Thread.sleep(Native Method)
         at java.lang.Thread.sleep(Thread.java:340)
         at java.util.concurrent.TimeUnit.sleep(TimeUnit.java:360)
         at thread.state.StudyStateWaitingToBlocked$2.run(StudyStateWaitingToBlocked.java:34)
         - locked <0x00000007aabbf950> (a java.lang.Object)

         "A" prio=5 tid=0x00007fd425822000 nid=0x5503 waiting for monitor entry [0x00007000023b7000]
         java.lang.Thread.State: BLOCKED (on object monitor)
         at java.lang.Object.wait(Native Method)
         - waiting on <0x00000007aabbf950> (a java.lang.Object)
         at thread.state.StudyStateWaitingToBlocked$1.run(StudyStateWaitingToBlocked.java:21)
         - locked <0x00000007aabbf950> (a java.lang.Object)

         */
       Thread tA = new Thread("A"){
            @Override
            public void run() {
                synchronized (lock){
                    System.out.println(this.getName()+" 获取锁");
                    try {
                        System.out.println("A 睡眠 开始，执行 sleep");
                        TimeUnit.SECONDS.sleep(5);
                        System.out.println("A 睡眠 结束，执行wait 放弃锁");
                        lock.wait();// wait 有参与无参的区别， wait() 状态为 WAITING
//                      lock.wait(2*1000);// 状态为 TIMED_WAITING
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };


       Thread tB =  new Thread("B"){
            @Override
            public void run() {
                synchronized (lock){
                    System.out.println(this.getName()+" 获取锁");
                    try {
                        System.out.println("B 睡眠 开始，执行 sleep");
                        TimeUnit.SECONDS.sleep(5);
                        System.out.println("B 睡眠 结束。");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };



        Thread tC = new Thread("C"){
            @Override
            public void run() {
                synchronized (lock){
                    System.out.println(this.getName()+" 获取锁");
                    try {
                        System.out.println("C 睡眠 开始，执行 sleep");
                        TimeUnit.SECONDS.sleep(5);
                        System.out.println("C 睡眠 结束，执行 notify");
                        lock.notify();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        tA.start();
        tB.start();
        tC.start();


        for (;;){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(tA.getName()+"="+tA.getState().toString()+", "+tB.getName()+"="+tB.getState()+", "+tC.getName()+"="+tC.getState());
            if(tA.getState().toString()=="TERMINATED" && tB.getState().toString()=="TERMINATED" && tC.getState().toString()=="TERMINATED" ){
                break;
            }
        }




    }



}
