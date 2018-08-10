package sample.dp.proxy.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author soyona
 * @Package sample.dp.proxy.cglib
 * @Desc:
 * @date 2018/6/21 22:00
 */
public class PriceServiceProxy  implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("尝试从缓存中获取价格信息。");
        //调用原始对象 的方法
        return methodProxy.invokeSuper(o, objects);
    }
}
