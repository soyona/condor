package sample.jcu.synchronization;

import java.util.concurrent.TimeUnit;

/**
 * @author soyona
 * @Package sample.jcu.synchronization
 * @Desc:
 * 共享变量 non-volatile variable = 1
 *
 * 线程1 循环输出
 * 线程2 修改 variable =2
 *
 *
 * 结果 线程1 也能得知
 *
 * @date 2018/8/3 11:49
 */
public class HappensBeforeSync2 {
    private static int variable = 1; // this variable is
    // strictly not volatile

    public static void main(String args[]) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    System.out.println(variable);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start(); // line 17
        new java.lang.Thread(new java.lang.Runnable() {

            @Override
            public void run() {
//                try {
//                    TimeUnit.SECONDS.sleep(2);
//                } catch (java.lang.InterruptedException e) {
//                }
                for(;;){
                    variable++;
                }

                // writer-thread now terminates,
                // is it guaranteed that when it
                // "terminates successfully", variable
                // is updated on the reader-thread ?
            }
        }).start();
    }
}
