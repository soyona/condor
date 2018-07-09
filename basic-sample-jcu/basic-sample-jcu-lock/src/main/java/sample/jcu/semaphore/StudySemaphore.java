package sample.jcu.semaphore;

/**
 * Created by kanglei on 20/10/2017.
 */
public class StudySemaphore {



    public void did(){
        SemaphoreService service = new SemaphoreService();
        AThread aThread = new AThread(service);
        aThread.setName("A");
        aThread.start();

        BThread bThread = new BThread(service);
        bThread.setName("B");
        bThread.start();
    }




    public static void main(String[] args) {

        StudySemaphore study = new StudySemaphore();
        study.did();
    }
















    class AThread extends Thread {
        SemaphoreService service;
        public AThread(SemaphoreService service){
            this.service = service;
        }

        @Override
        public void run() {
            this.service.test();
        }
    }

    class BThread extends Thread {
        SemaphoreService service;
        public BThread(SemaphoreService service){
            this.service = service;
        }

        @Override
        public void run() {
            this.service.test();
        }
    }

}
