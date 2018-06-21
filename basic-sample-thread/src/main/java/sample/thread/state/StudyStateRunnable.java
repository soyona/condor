package sample.thread.state;

import sample.thread.Utils;

/**
 * Created by kanglei on 13/10/2017.
 */
public class StudyStateRunnable extends Thread{
    @Override
    public void run() {
        super.run();
        for(;;){
            this.setName("KangleiThread");
            System.out.println("working .."+ Utils.pid());
        }
    }

    public static void main(String[] args) {
        StudyStateRunnable ss = new StudyStateRunnable();

                ss.start();
    }
}
