package sample.jcu.threadpool;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author soyona
 * @Package sample.jcu.threadpool
 * @Desc:
 * @date 2018/8/14 21:51
 */
public class StudyTimer {

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                System.out.println("..");
                System.gc();
            }
        },1000);

    }

}
