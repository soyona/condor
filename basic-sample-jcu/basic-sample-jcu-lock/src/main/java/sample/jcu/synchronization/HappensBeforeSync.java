package sample.jcu.synchronization;

import java.util.concurrent.TimeUnit;

/**
 * @author soyona
 * @Package sample.jcu.synchronization
 * @Desc:
 * @date 2018/8/3 09:57
 */
public class HappensBeforeSync {
    //共享变量
    public static boolean flag=true;
    static class ThreadA extends Thread{
        @Override
        public void run() {
            int i=0;
           while(flag){
               i++;
               /**
                * 无论用Sleep还是System.out.println()，while(flag)可以终止
                * 但是：flag 并不是volatile变量，flag在主线程工作内存的修改（assign指令）能被子线程ThreadA感知，
                *
                * println()实现中有synchronized，但是对System.out对象，不是共享变量
                * 如果synchronized语义是清除线程ThreadA的工作内存，保持是最新的变量，但是主线程对assign并没同步到主存，怎么能是最新的？
                *
                * 确实在此处 无论用Sleep还是System.out.println()，都可以拿到flag的最新值，这是JVM的优化吗？
                *
                * 可以确定的是：synchronized 无论是不是对共享变量加锁，在lock action时 但是其语义是清除工作缓存，并从主存读取最新变量；
                * 问题是？：主线程对共享变量的更新，何时同步到主存，触发同步的机制是什么，JMM的happen-before没有指明此种情况的可见性
                */
//             //1.System.out.println 情况
//               System.out.println(i);

               //2.Sleep情况
               try {
                   TimeUnit.SECONDS.sleep(3);
                   System.out.println(i);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        }
    }
    public static void main(String[] args) {
        new ThreadA().start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // flag 是 non-volatile 什么时候刷新到 主存，让其他线程 看见更新后的值
        flag = false;
        System.out.println("main end."+flag+" "+System.currentTimeMillis());
    }
}
