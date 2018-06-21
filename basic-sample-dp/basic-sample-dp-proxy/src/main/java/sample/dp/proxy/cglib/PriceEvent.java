package sample.dp.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;

/**
 * @author soyona
 * @Package sample.dp.proxy.cglib
 * @Desc:
 * @date 2018/6/21 22:06
 */
public class PriceEvent {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(PriceService.class);
        enhancer.setCallback(new PriceServiceProxy());

        //通过 enhancer.create() 创建实际的代理对象
        PriceService priceService = (PriceService)enhancer.create();
        priceService.getPromotionPrice();
    }
}
