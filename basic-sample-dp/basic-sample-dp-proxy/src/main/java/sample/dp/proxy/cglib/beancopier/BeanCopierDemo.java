package sample.dp.proxy.cglib.beancopier;

import net.sf.cglib.beans.BeanCopier;

import java.math.BigDecimal;

/**
 * @author soyona
 * @Package sample.dp.proxy.cglib.beancopier
 * @Desc:
 * @date 2018/7/3 13:01
 */
public class BeanCopierDemo {
    public static void main(String[] args) {
        Order order = new Order("2019",new BigDecimal(23));
        TOrder tOrder = new TOrder();
        BeanCopier copier = BeanCopier.create(order.getClass(),tOrder.getClass(),false);
        //getter/setter
        copier.copy(order,tOrder,null);
        System.out.println(tOrder);

    }
}
