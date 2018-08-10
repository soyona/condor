package sample.rabbitmq;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author soyona
 * @Package sample.rabbitmq
 * @Desc:
 * @date 2018/8/2 21:09
 */
public class RMQUtils {
    public static final String QUEUENAME_OMS="OMS";
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

    public static void send(String queueName,String msg)throws Exception{
        Channel channel =  conn.createChannel();
//        channel.exchangeDeclare();
        channel.queueDeclare(queueName,false,false,false,null);
        channel.basicPublish("",queueName,null,msg.getBytes());
        System.out.println("Sent  "+ msg);
        channel.close();
    }

    public static void consume(String queueName) throws IOException {
        Channel channel =  conn.createChannel();
        channel.queueDeclare(queueName,false,false,false,null);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Customer Received '" + message + "'");
            }
        };
        //自动回复队列应答 -- RabbitMQ中的消息确认机制
        channel.basicConsume(queueName, true, consumer);

    }


    private static Connection conn() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST_IP);
        factory.setPort(HOST_PORT);
        Connection connection = null;
        try {
            connection = factory.newConnection();
            return  connection;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return connection;
    }



    @Test
    public void testCon(){
        System.out.println(conn());;
    }

}
