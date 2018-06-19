package sample.pd.observer;

/**
 * @author soyona
 * @Package sample.pd.observer
 * @Desc: 京东渠道的 价格观察者，监听到变化，调用京东价格API进行修改行为
 * @date 2018/6/19 20:55
 */
public class JDPriceObserver implements PriceObserver {
    public JDPriceObserver(){
        System.out.println("初始化京东价格修改观察者");
    }
    @Override
    public void update() {
        System.out.println("京东观察者收到价格修改通知。");
        System.out.println("调用京东OPEN API 修改价格。");
    }
}
