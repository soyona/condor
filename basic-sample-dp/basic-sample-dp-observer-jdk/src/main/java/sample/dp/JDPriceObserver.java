package sample.dp;

import java.util.Observable;
import java.util.Observer;

/**
 * @author soyona
 * @Package sample.dp.observer
 * @Desc: 京东渠道的 价格观察者，监听到变化，调用京东价格API进行修改行为
 * @date 2018/6/19 20:55
 */
public class JDPriceObserver implements Observer {
    public JDPriceObserver(){
        System.out.println("初始化京东价格修改观察者");
    }


    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("JD观察者收到价格修改通知。"+ arg);
        System.out.println("调用JD OPEN API 修改价格。"+ arg);
    }
}
