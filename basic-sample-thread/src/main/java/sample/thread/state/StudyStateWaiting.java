package sample.thread.state;

/**
 * Created by kanglei on 13/10/2017.
 */
public class StudyStateWaiting {
    final static Object lock = new Object();


    public static void main(String[] args) {
        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

