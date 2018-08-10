package sample.rabbitmq.price;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author soyona
 * @Package sample.rabbitmq.price
 * @Desc:
 * @date 2018/8/2 22:54
 */
public class ElasticSearchPriceConsume {
    @Test
    public void accept(){
        try {
            PriceMQ.accept();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void pull(){
        try {
            while (true){
                TimeUnit.SECONDS.sleep(1);
                PriceMQ.pull();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
