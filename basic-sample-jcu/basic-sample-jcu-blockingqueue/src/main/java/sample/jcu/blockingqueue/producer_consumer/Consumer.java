package sample.jcu.blockingqueue.producer_consumer;

import java.util.concurrent.BlockingQueue;

/**
 * @author soyona
 * @Package sample.jcu.blockingqueue.producer_consumer
 * @Desc:
 * @date 2018/7/30 15:10
 */
public class Consumer implements Runnable{
    BlockingQueue queue;
    public Consumer(BlockingQueue queue){
        this.queue = queue;
    }
    @Override
    public void run() {
        try {
            while(true){
                String s = (String)queue.take();
                System.out.println("take :"+s);
            }
        }catch (Exception e){

        }
    }
}
