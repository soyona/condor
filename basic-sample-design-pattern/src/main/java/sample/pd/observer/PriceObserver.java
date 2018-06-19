package sample.pd.observer;

/**
 * @author soyona
 * @Package sample.pd.observer
 * @Desc: 价格观察者，观察价格信息有变化，做出响应的动作；价格信息变化时，天猫、京东、美丽说等渠道价格要随之变化，此时需要各渠道提供更新方法
 * @date 2018/6/19 20:50
 */
public interface PriceObserver {
    public void update();
}
