package sample.jcu.unsafe;

import org.junit.Test;
import sun.misc.Unsafe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author soyona
 * @Package sample.jcu.unsafe
 * @Desc: creating an instance by Unsafe
 * @date 2018/7/13 15:42
 */
public class GetInstanceDemo {
    class Order{
        private String orderNo = "2019";
        public String getOrderNo(){
            System.out.println("calling the method of getOrderNo()");
            return this.orderNo;
        }
    }

    @Test
    public  void instanceTest() {
        try {
            Order order = (Order)UnsafeUtils.unsafe.allocateInstance(Order.class);
            assertNull(order.getOrderNo());
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
