package sample.rabbitmq.price;

import org.junit.Test;

/**
 * @author soyona
 * @Package sample.rabbitmq.price
 * @Desc:
 * @date 2018/8/2 22:35
 */
public class PriceUpdateService {
    @Test
    public void updatePrice(){
        try {
            for (int i=0;i<10;i++){
                PriceMQ.send("Price changed to "+i+10);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
