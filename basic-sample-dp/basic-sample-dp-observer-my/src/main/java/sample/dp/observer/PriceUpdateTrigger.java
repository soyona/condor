package sample.dp.observer;

/**
 * @author soyona
 * @Package sample.dp.observer
 * @Desc: 触发价格修改
 * @date 2018/6/19 21:11
 */
public class PriceUpdateTrigger {
    public static void main(String[] args) {
        //初始化观察者
        PriceObserver po_1 = new JDPriceObserver();
        PriceObserver po_2 = new TmallPriceObserver();

        //初始化被观察者
        PromotionPriceService ppservice = new PromotionPriceService();
        //被观察者，添加观察者
        ppservice.registerObserver(po_1);
        ppservice.registerObserver(po_2);

        //触发价格更新
        ppservice.update();
    }
}
