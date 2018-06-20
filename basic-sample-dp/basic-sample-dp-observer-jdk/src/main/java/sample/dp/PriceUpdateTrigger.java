package sample.dp;

import java.util.Observer;

/**
 * @author soyona
 * @Package sample.dp.observer
 * @Desc: 触发价格修改
 * @date 2018/6/19 21:11
 */
public class PriceUpdateTrigger {
    public static void main(String[] args) {
        //初始化观察者
        Observer po_1 = new JDPriceObserver();
        Observer po_2 = new TmallPriceObserver();

        //初始化被观察者
        PromotionPriceService ppservice = new PromotionPriceService();
        //被观察者，添加观察者
        ppservice.addObserver(po_1);
        ppservice.addObserver(po_2);

        //触发价格更新
        ppservice.update();
    }
}
