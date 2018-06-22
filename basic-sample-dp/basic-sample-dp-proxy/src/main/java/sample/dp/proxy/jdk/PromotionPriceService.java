package sample.dp.proxy.jdk;

/**
 * @author soyona
 * @Package sample.dp.proxy.jdk
 * @Desc: 被代理的类
 * @date 2018/6/22 13:48
 */
public class PromotionPriceService implements PriceService {
    /**
     * 更新价格
     */
    public void update() {
        System.out.println("执行变价。。");
    }

    public void remove() {
        System.out.println("删除价格。。");
    }
}
