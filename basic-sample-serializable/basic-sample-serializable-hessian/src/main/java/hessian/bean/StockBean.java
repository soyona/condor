package hessian.bean;

import java.io.Serializable;

/**
 * @author soyona
 * @Package hessian.bean
 * @Desc:
 * @date 2018/9/1 21:43
 */
public class StockBean extends PageModel implements Serializable{

    private long pro_sum;
    private int shop_sid;

    public long getPro_sum() {
        return pro_sum;
    }

    public void setPro_sum(long pro_sum) {
        this.pro_sum = pro_sum;
    }
}
