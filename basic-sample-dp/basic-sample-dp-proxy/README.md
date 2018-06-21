# CGLib实现动态代理
## 特点
> CGLIB通过继承的方式进行代理，无论目标对象有没有实现接口都可以代理，但是无法处理final的情况。
 
> CGLib生成代理类，是PriceService.class 的子类，
enhancer.setSuperclass(PriceService.class);
 
> 
```java
Enhancer enhancer = new Enhancer();
enhancer.setSuperclass(PriceService.class);
enhancer.setCallback(new PriceServiceProxy());

//通过 enhancer.create() 创建实际的代理对象
PriceService priceService = (PriceService)enhancer.create();
priceService.getPromotionPrice();
```

# JDK动态代理
## 特点
> JDK原生动态代理是Java原生支持的，不需要任何外部依赖，但是它只能基于接口进行代理；

## Reference
> http://www.importnew.com/16735.html

# JDK8 动态代理
## Reference
> http://www.importnew.com/16670.html