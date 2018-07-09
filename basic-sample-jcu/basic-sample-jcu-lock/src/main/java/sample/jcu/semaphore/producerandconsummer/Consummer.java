package sample.jcu.semaphore.producerandconsummer;

/**
 * Created by kanglei on 20/10/2017.
 */
public class Consummer extends Thread {
    FootList list = null;
    public Consummer(FootList list){
        this.list = list;
    }

    @Override
    public void run() {
        this.list.eat();
    }
}
