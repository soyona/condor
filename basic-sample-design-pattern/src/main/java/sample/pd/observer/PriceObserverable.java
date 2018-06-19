package sample.pd.observer;

/**
 * @author soyona
 * @Package sample.pd.observer
 * @Desc: 价格修改服务，是被观察者，被观察者应该具备有 决定拥有哪些观察者的权利，
 * @date 2018/6/19 20:57
 */
public interface PriceObserverable {
    //注册观察者
    public void registerObserver(PriceObserver priceObserver);
    //删除观察者
    public void removeObserver(PriceObserver priceObserver);
    //通知观察者
    public void notifyObserver();
}
