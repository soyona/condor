package sample.jcu.semaphore.producerandconsummer;

/**
 * Created by kanglei on 20/10/2017.
 */
public class StudyMain {

    public static void main(String[] args) {
        FootList footList = new FootList();
        Producer[] producers = new Producer[30];
        for (int i = 0; i <producers.length ; i++) {
            producers[i] = new Producer(footList);
            producers[i].setName("cook-"+i);
            producers[i].start();
        }

        Consummer[] consummers = new Consummer[30];
        for (int i = 0; i < consummers.length; i++) {
            consummers[i] = new Consummer(footList);
            consummers[i].setName("dinner-"+i);
            consummers[i].start();
        }
    }
}
