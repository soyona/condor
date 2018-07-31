package sample.rmi.bean;

import java.io.Serializable;

/**
 * @author soyona
 * @Package sample.rmi.bean
 * @Desc:
 * @date 2018/6/12 14:55
 */
public class Order implements Serializable{
    private String no;
    private Double money;
    /**
     * 非序列化
     */
    private transient int status;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("[")
                .append("no=").append(this.no).append(",")
                .append("money=").append(this.money).append(",")
                .append("status=").append(this.status).append("]").toString();
    }
}
