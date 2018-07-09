package jcu.app.producer_consummer;

/**
 * @author soyona
 * @Package jcu.app.producer_consummer
 * @Desc:
 * @date 2018/7/9 19:20
 */
public class PCDemo {

    static class Consummer extends Thread{
        ListBucket bucket;
        public Consummer(ListBucket bucket){
            this.bucket = bucket;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 100; i++) {
                    bucket.get();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Producer extends Thread{
        ListBucket bucket;
        public Producer(ListBucket bucket){
            this.bucket = bucket;
        }
        @Override
        public void run() {
            try {
                for (int i = 0; i < 100 ; i++) {
                    bucket.put();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    public static void main(String[] args) {
        ListBucket bucket = new ListBucket();
        Consummer consummer = new Consummer(bucket);
        Producer producer = new Producer(bucket);
        consummer.start();
        producer.start();


    }
}
