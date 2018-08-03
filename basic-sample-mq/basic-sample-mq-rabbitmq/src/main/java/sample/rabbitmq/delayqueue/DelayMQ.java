package sample.rabbitmq.delayqueue;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author soyona
 * @Package sample.rabbitmq.price
 * @Desc:
 * @date 2018/8/2 22:37
 */
public class DelayMQ {
    public static final String X_NAME = "X_C";
    public static final String Q_NAME="q_1";
    public static final String Q_NAME_DLX="q_2";
    public static final String USERNAME="guest";
    public static final String PWD="guest";
    public static final String HOST_IP = "localhost";
    public static final int HOST_PORT = 5672;

    private static Connection conn;

    static {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST_IP);
        factory.setPort(HOST_PORT);
        Connection connection = null;
        try {
            conn = factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public static void send(String msg)throws Exception{
        Channel channel =  conn.createChannel();
        /**
         * 声明交换器 direct
         */
        channel.exchangeDeclare(X_NAME,"direct",true,false,null);
        /**
         * 声明队列
         */
        channel.queueDeclare(Q_NAME,true,false,false,null);
        /**
         * 声明 交换器 队列绑定
         */
        channel.queueBind(Q_NAME,X_NAME,"updatePrice");
        /**
         * 发布消息,
         * deliveryMode 2 持久化到磁盘
         * 6秒 失效
         */
        channel.basicPublish(X_NAME,"updatePrice",
                new AMQP.BasicProperties.Builder()
                .expiration("6000")
                        .contentType("text/plain").deliveryMode(2)
                .priority(1)
                .build(),msg.getBytes());
        System.out.println("Sent  "+ msg);
        channel.close();
    }


    public static void accept() throws IOException, TimeoutException {
        final Channel channel =  conn.createChannel();
        channel.basicQos(10);
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Customer Received '" + message + "'");
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        /**
         * 1：队列
         * 2：autoAck=false
         * 3：消费者标签
         * 4：回调函数
         */
        channel.basicConsume(Q_NAME,false,"",consumer);
        channel.close();

    }

    /**
     * 拉模式
     * @throws IOException
     * @throws TimeoutException
     */
    public static void pull() throws IOException, TimeoutException {
        final Channel channel =  conn.createChannel();

        /**
         * 1.队列名称
         * 2.autoAck
         *
         */
        GetResponse response = channel.basicGet(Q_NAME,false);
        if(response !=null){
            System.out.println("拉取到："+new String(response.getBody()));
            //发送确认
            channel.basicAck(response.getEnvelope().getDeliveryTag(),false);
            //发送拒绝
            channel.basicReject(response.getEnvelope().getDeliveryTag(),false);
        }
        channel.close();

    }

}
