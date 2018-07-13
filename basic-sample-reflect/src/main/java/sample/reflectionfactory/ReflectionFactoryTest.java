package sample.reflectionfactory;

import org.junit.Test;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;

/**
 * @author soyona
 * @Package sample.reflectionfactory
 * @Desc:
 * @date 2018/7/13 17:05
 */
public class ReflectionFactoryTest {
    class Order{
        String orderNo = "10";
        private Order(){
            System.out.println("private constructor.");
        }
        public String getOrderNo(){
            return this.orderNo;
        }
    }

    /**
     *
     * Using constructor: Object.class.getConstructor()
     *
     *
     */
    @Test
    public void makeInstance(){
        try {
            Constructor constructor = ReflectionFactory.getReflectionFactory().newConstructorForSerialization(Order.class,Object.class.getConstructor());
            constructor.setAccessible(true);
            Order order = (Order)constructor.newInstance();
            order.getOrderNo();
            assertEquals("10",order.getOrderNo());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
