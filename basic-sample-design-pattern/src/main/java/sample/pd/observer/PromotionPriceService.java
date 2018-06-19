package sample.pd.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author soyona
 * @Package sample.pd.observer
 * @Desc:
 * 促销价格维护服务，应该具备维护价格的基本行为，价格修改通知观察者的行为
 * @date 2018/6/19 21:02
 */
public class PromotionPriceService implements PriceService,PriceObserverable {

    //收集观察者的集合
    private List<PriceObserver> observerList = new ArrayList<PriceObserver>();

    /**
     * 注册观察者
     * @param priceObserver
     */
    @Override
    public void registerObserver(PriceObserver priceObserver) {
        observerList.add(priceObserver);
    }

    /**
     * 删除指定观察者
     * @param priceObserver
     */
    @Override
    public void removeObserver(PriceObserver priceObserver) {
        observerList.remove(priceObserver);
    }

    /**
     * 通知所有观察者
     */
    @Override
    public void notifyObserver() {
        for(PriceObserver o:observerList){
            o.update();
        }
    }

    @Override
    public void update() {
        //1.价格更新服务实现
        System.out.println("价格修改成功。");
        //2.通知观察者
        notifyObserver();
    }
}
