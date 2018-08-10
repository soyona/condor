package sample.rabbitmq;

import org.junit.Test;

/**
 * @author soyona
 * @Package sample.rabbitmq
 * @Desc:
 * @date 2018/8/2 20:52
 */
public class KProducer {


    @Test
    public void produce(){
        try {
            while(true){

                RMQUtils.send(RMQUtils.QUEUENAME_OMS,"20180803323");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
