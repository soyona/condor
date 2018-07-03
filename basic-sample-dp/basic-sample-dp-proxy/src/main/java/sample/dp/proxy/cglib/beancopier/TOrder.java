package sample.dp.proxy.cglib.beancopier;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author soyona
 * @Package sample.dp.proxy.cglib.beancopier
 * @Desc:
 * @date 2018/7/3 13:00
 */
public class TOrder {
    private String orderNo;

    public TOrder() {
    }

    public TOrder(String orderNo, BigDecimal money, Date createTime) {
        this.orderNo = orderNo;
        this.money = money;
        this.createTime = createTime;
    }
//
//    public String getOrderNo() {
//        return orderNo;
//    }
//
//    public void setOrderNo(String orderNo) {
//        this.orderNo = orderNo;
//    }

//    public BigDecimal getMoney() {
//        return money;
//    }
//
//    public void setMoney(BigDecimal money) {
//        this.money = money;
//    }
//
//    public Date getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Date createTime) {
//        this.createTime = createTime;
//    }

    private BigDecimal money;
    private Date createTime;


    @Override
    public String toString(){
        return "[orderNo="+orderNo+",money="+money+",createTime="+createTime+"]";
    }
}
