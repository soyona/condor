# 观察者模式
## 实现举例
> sample.pd.observer.*
## 角色
 - [x]  观察者接口定义: sample.pd.observer.PriceObserver
 - [x]  具体观察者实现1: sample.pd.observer.JDPriceObserver
 - [x]  具体观察者实现2: sample.pd.observer.TmallPriceObserver
 - [x]  被观察者接口定义：sample.pd.observer.PriceObserverable
 - [x]  具体被观察者：sample.pd.observer.PromotionPriceService implements PriceObserverable
 
## 意义
> 参考：[Java程序性能优化]P34
>观察者模式可以用于事件监听、通知发布等场合。可以确保观察者在不适用轮询监控的情况下，及时收到相关消息和事件。

