package sample.rabbitmq;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author soyona
 * @Package sample.rabbitmq
 * @Desc:
 * @date 2018/8/2 20:53
 */
public class KConsumer {
    @Test
    public void accept() throws IOException, InterruptedException {
        while (true){
            TimeUnit.SECONDS.sleep(2);
            RMQUtils.consume(RMQUtils.QUEUENAME_OMS);
        }
    }
}
