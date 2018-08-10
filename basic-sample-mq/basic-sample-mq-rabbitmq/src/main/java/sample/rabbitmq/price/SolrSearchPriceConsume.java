package sample.rabbitmq.price;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author soyona
 * @Package sample.rabbitmq.price
 * @Desc:
 * @date 2018/8/2 22:55
 */
public class SolrSearchPriceConsume {
    @Test
    public void accept(){
        try {
            while (true){

                PriceMQ.accept();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
