package sample.jcu.blockingqueue.producer_consumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author soyona
 * @Package sample.jcu.blockingqueue.producer_consumer
 * @Desc:
 * @date 2018/7/30 15:13
 */
public class TestProducerAndConsumer {
    public static void main(String[] args) {
        //1.初始化阻塞队列,大小 10，true 公平锁
        BlockingQueue queue = new ArrayBlockingQueue(3,true);
        //2.初始化生产者
        Producer p = new Producer(queue);
        //3.初始化消费者
        Consumer c = new Consumer(queue);
        new Thread(p).start();
        new Thread(c).start();
    }
}
