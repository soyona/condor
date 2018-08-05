package sample.queue;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import sample.api.RedisUtils;

import java.util.UUID;

/**
 * @author soyona
 * @Package sample.queue
 * @Desc:
 * @date 2018/8/5 21:56
 */
public class QueueTest {
    @Test
    public void testQueue(){
        Jedis jedis = RedisUtils.getJedis();
        //清除key
        jedis.del("name");
        jedis.lpush("name","A","B","C");
        System.out.println(jedis.lrange("name",0,-1));
        jedis.rpoplpush("name","new_name");
//        System.out.println(jedis.rpop("new_name"));;
        System.out.println(jedis.lrange("name",0,-1));
        System.out.println(jedis.lrange("new_name",0,-1));

        RedisUtils.backJedis(jedis);
    }


    //生产者线程
    static class PThread extends Thread{
        QService service;
        public PThread(QService service){
            this.service = service;
        }
        @Override
        public void run() {
            for(int i=0;i<10;i++){
                service.produce();
            }
        }
    }
    //消费者线程
   static class CThread extends Thread{
        QService service;
        public CThread(QService service){
            this.service = service;
        }
        @Override
        public void run() {
            while(true){
                String item = service.consume();
                System.out.println(Thread.currentThread().getId()+" 获取消息： "+item);
            }
        }
    }

    static class QService{
        String q_name;
        public QService(String queueName){
            this.q_name = queueName;
        }
        public void produce(){
            Jedis jedis = RedisUtils.getJedis();
            jedis.lpush(q_name, UUID.randomUUID().toString());
            RedisUtils.backJedis(jedis);
        }

        public String consume(){
            Jedis jedis = RedisUtils.getJedis();
            String item = jedis.rpop(q_name);
            RedisUtils.backJedis(jedis);
            return item;
        }
    }


    public static void main(String[] args) {
        QService qService = new QService("REQUEST_ORDER");
        // 100 个客户端线程
        final int clients = 100;
        Thread[] producers = new Thread[clients];
        for (int i = 0; i < clients ; i++) {
            producers[i] = new PThread(qService);
        }

        for (int i = 0; i < clients ; i++) {
            producers[i].start();
        }

        //
        final int servers = 1;
        Thread[] consumers = new Thread[servers];
        for (int i = 0; i < servers ; i++) {
            consumers[i] = new CThread(qService);
        }

        for (int i = 0; i < servers ; i++) {
            consumers[i].start();
        }
    }
}

