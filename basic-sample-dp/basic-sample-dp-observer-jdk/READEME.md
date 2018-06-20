# JDK观察者模式

## 角色
### 被观察者
> 继承 java.util.Observable 类，而不是接口
 
> **setChanged()**
```java
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
```

### 观察者
> 实现java.util.Observer接口
```java
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
```

### 触发
```java
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
```

### JDK 实现 notifyObservers
```java
public void notifyObservers(Object arg) {
        /*
         * a temporary array buffer, used as a snapshot of the state of
         * current Observers.
         */
        Object[] arrLocal;

        synchronized (this) {
            /* We don't want the Observer doing callbacks into
             * arbitrary code while holding its own Monitor.
             * The code where we extract each Observable from
             * the Vector and store the state of the Observer
             * needs synchronization, but notifying observers
             * does not (should not).  The worst result of any
             * potential race-condition here is that:
             * 1) a newly-added Observer will miss a
             *   notification in progress
             * 2) a recently unregistered Observer will be
             *   wrongly notified when it doesn't care
             */
            if (!changed)
                return;
            arrLocal = obs.toArray();
            clearChanged();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((Observer)arrLocal[i]).update(this, arg);
}
```
