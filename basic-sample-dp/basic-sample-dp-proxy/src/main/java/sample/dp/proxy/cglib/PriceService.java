package sample.dp.proxy.cglib;

/**
 * @author soyona
 * @Package sample.dp.proxy.cglib
 * @Desc:
 * @date 2018/6/21 21:58
 */
public class PriceService {
    public String getPromotionPrice(){
        System.out.println("获取促销价格。");
        return "200";
    }
}
