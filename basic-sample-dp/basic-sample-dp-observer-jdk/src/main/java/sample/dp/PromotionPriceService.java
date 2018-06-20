package sample.dp;

import java.util.Observable;

/**
 * @author soyona
 * @Package sample.dp
 * @Desc: 价格更新服务，被观察者角色
 * @date 2018/6/20 16:52
 */
public class PromotionPriceService extends Observable implements PriceService{
    @Override
    public void update() {
        System.out.println("更新价格事务行为。。。。");
        //1. 设置更新标志
        setChanged();
        //2. 通知观察者
        notifyObservers("200");
    }
}
