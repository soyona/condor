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
## 源码分析
### 接口InvocationHandler
```java
public interface InvocationHandler {

    /**
     * Processes a method invocation on a proxy instance and returns
     * the result.  This method will be invoked on an invocation handler
     * when a method is invoked on a proxy instance that it is
     * associated with.
     *
     * @param   proxy the proxy instance that the method was invoked on
     *
     * @param   method the {@code Method} instance corresponding to
     * the interface method invoked on the proxy instance.  The declaring
     * class of the {@code Method} object will be the interface that
     * the method was declared in, which may be a superinterface of the
     * proxy interface that the proxy class inherits the method through.
     *
     * @param   args an array of objects containing the values of the
     * arguments passed in the method invocation on the proxy instance,
     * or {@code null} if interface method takes no arguments.
     * Arguments of primitive types are wrapped in instances of the
     * appropriate primitive wrapper class, such as
     * {@code java.lang.Integer} or {@code java.lang.Boolean}.
     *
     * @return  the value to return from the method invocation on the
     * proxy instance.  If the declared return type of the interface
     * method is a primitive type, then the value returned by
     * this method must be an instance of the corresponding primitive
     * wrapper class; otherwise, it must be a type assignable to the
     * declared return type.  If the value returned by this method is
     * {@code null} and the interface method's return type is
     * primitive, then a {@code NullPointerException} will be
     * thrown by the method invocation on the proxy instance.  If the
     * value returned by this method is otherwise not compatible with
     * the interface method's declared return type as described above,
     * a {@code ClassCastException} will be thrown by the method
     * invocation on the proxy instance.
     *
     * @throws  Throwable the exception to throw from the method
     * invocation on the proxy instance.  The exception's type must be
     * assignable either to any of the exception types declared in the
     * {@code throws} clause of the interface method or to the
     * unchecked exception types {@code java.lang.RuntimeException}
     * or {@code java.lang.Error}.  If a checked exception is
     * thrown by this method that is not assignable to any of the
     * exception types declared in the {@code throws} clause of
     * the interface method, then an
     * {@link UndeclaredThrowableException} containing the
     * exception that was thrown by this method will be thrown by the
     * method invocation on the proxy instance.
     *
     * @see     UndeclaredThrowableException
     */
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable;
}
```
### 类Proxy

## 特点
> JDK原生动态代理是Java原生支持的，不需要任何外部依赖，但是它只能基于接口进行代理；


## Reference
> http://www.importnew.com/16735.html

# JDK8 动态代理
## Reference
> http://www.importnew.com/16670.html