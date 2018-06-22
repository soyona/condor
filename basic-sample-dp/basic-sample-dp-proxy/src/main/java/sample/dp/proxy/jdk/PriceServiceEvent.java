package sample.dp.proxy.jdk;

import java.lang.reflect.Proxy;

/**
 * @author soyona
 * @Package sample.dp.proxy.jdk
 * @Desc:
 * @date 2018/6/22 15:38
 */
public class PriceServiceEvent {
    public static void main(String[] args) {
        PriceService priceService = new PromotionPriceService();
        MyInvocationHandler myInvocationHandler = new MyInvocationHandler(priceService);

        //Proxy创建代理类的实例，代理类实现PriceService接口
        PriceService priceServiceProxy = (PriceService) Proxy.newProxyInstance(PriceServiceEvent.class.getClassLoader(),
                new Class[]{PriceService.class},myInvocationHandler);
        //代理类实例调用 update();
        priceServiceProxy.update();
        priceServiceProxy.remove();
    }
}
