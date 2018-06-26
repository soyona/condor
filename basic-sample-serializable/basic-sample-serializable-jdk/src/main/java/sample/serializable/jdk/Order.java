package sample.serializable.jdk;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author soyona
 * @Package sample.serializable.jdk
 * @Desc:
 * @date 2018/6/22 17:32
 */
public class Order extends Parent implements Serializable {
    private static final long serialVersionUID = -5573274132416494354L;

    public Order() {
        System.out.println("默认构造。。");
    }

    public String orderNo;
    public final Date createdTime = new Date();
    public BigDecimal needSaleMoney;
    public transient int status;
    public static String PNO;
    //对象引用的序列化
    public Member member;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static String getPNO() {
        return PNO;
    }

    public static void setPNO(String PNO) {
        Order.PNO = PNO;
    }

    public BigDecimal getNeedSaleMoney() {
        return needSaleMoney;
    }

    public void setNeedSaleMoney(BigDecimal needSaleMoney) {
        this.needSaleMoney = needSaleMoney;
    }

    @Override
    public String toString(){
      StringBuffer sb = new StringBuffer("[").
              append("orderNo=").append(this.orderNo).append(",").
              append("createdTime=").append(this.createdTime).append(",").
              append("needSaleMoney=").append(this.needSaleMoney).append(",").
              append("transient->status=").append(this.status).append(",").
              append("Parent.CNO=").append(getCNO()).append(",").
              append("static PNO=").append(PNO).
              append("]");
      return sb.toString();

    }
}
