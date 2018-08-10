package sample.rabbitmq.price;

import org.junit.Test;

/**
 * @author soyona
 * @Package sample.rabbitmq.price
 * @Desc:
 * @date 2018/8/2 23:03
 */
public class TestPrice {
   static class PriceThread extends Thread{
        PriceUpdateService service;
        public PriceThread(PriceUpdateService service){
            this.service = service;
        }

        @Override
        public void run() {
           service.updatePrice();
        }
    }


    static class SolrThread extends Thread{
       SolrSearchPriceConsume solrSearchPriceConsume;
       public SolrThread(SolrSearchPriceConsume solrSearchPriceConsume){
           this.solrSearchPriceConsume = solrSearchPriceConsume;
       }

        @Override
        public void run() {
            solrSearchPriceConsume.accept();
        }
    }

    @Test
    public void testC(){
        new SolrThread(new SolrSearchPriceConsume()).start();
    }
    public static void main(String[] args) {
        new PriceThread(new PriceUpdateService()).start();
    }
}
