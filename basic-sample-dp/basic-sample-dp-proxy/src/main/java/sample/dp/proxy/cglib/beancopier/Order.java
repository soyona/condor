package sample.dp.proxy.cglib.beancopier;

import java.math.BigDecimal;

/**
 * @author soyona
 * @Package sample.dp.proxy.cglib.beancopier
 * @Desc:
 * @date 2018/7/3 12:59
 */
public class Order {
    private String orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    private BigDecimal money;

    public Order(String orderNo, BigDecimal money) {
        this.orderNo = orderNo;
        this.money = money;
    }



    @Override
    public String toString(){
        return "[orderNo="+orderNo+",money="+money+"]";
    }
}
