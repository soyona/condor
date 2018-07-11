# 观察者模式
## 观察者模式角色
- 主题接口：被观察者，定义通知行为，当状态发生变化时，通知观察者
- 具体主题：实现`主题接口`的方法；维护观察者列表，如添加、删除观察者
- 观察者接口：定义被观察者-具体主题 的通知行为接口，具体主题调用的观察者接口，update
- 具体观察者：观察者接口update的具体实现，业务逻辑
## 实现举例
> sample.dp.observer.*
## 角色
 - [x]  观察者接口定义: sample.dp.observer.PriceObserver
 - [x]  具体观察者实现1: sample.dp.observer.JDPriceObserver
 - [x]  具体观察者实现2: sample.dp.observer.TmallPriceObserver
 - [x]  被观察者接口定义：sample.dp.observer.PriceObserverable
 - [x]  具体被观察者：sample.dp.observer.PromotionPriceService implements PriceObserverable
 
## 意义
> 参考：[Java程序性能优化]P34
> 观察者模式可以用于事件监听、通知发布等场合。可以确保观察者在不适用轮询监控的情况下，及时收到相关消息和事件。

