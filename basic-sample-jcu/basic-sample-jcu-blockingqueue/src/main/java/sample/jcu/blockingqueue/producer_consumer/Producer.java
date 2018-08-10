package sample.jcu.blockingqueue.producer_consumer;

import java.util.concurrent.BlockingQueue;

/**
 * @author soyona
 * @Package sample.jcu.blockingqueue.producer_consumer
 * @Desc:
 * @date 2018/7/30 15:08
 */
public class Producer implements Runnable{
    BlockingQueue queue;
    public Producer(BlockingQueue queue){
        this.queue = queue;
    }
    @Override
    public void run() {
        try {
            while(true){
                System.out.println("put:");
                queue.put("KAKA");
            }
        }catch (Exception e){

        }
    }
}
