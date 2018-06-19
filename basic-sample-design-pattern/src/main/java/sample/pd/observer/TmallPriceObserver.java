package sample.pd.observer;

/**
 * @author soyona
 * @Package sample.pd.observer
 * @Desc: 天猫渠道的 价格观察者，监听到变化，调用天猫价格API进行修改行为
 * @date 2018/6/19 20:53
 */
public class TmallPriceObserver implements PriceObserver{
    public TmallPriceObserver(){
        System.out.println("初始化天猫价格修改观察者");
    }
    @Override
    public void update() {
        System.out.println("天猫观察者收到价格修改通知。");
        System.out.println("调用天猫OPEN API 修改价格。");
    }
}
