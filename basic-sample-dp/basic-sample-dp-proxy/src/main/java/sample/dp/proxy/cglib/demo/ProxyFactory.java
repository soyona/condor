package sample.dp.proxy.cglib.demo;

import net.sf.cglib.proxy.*;
import sun.plugin2.jvm.RemoteJVMLauncher;

import java.lang.reflect.Method;

/**
 * @author soyona
 * @Package sample.dp.proxy.cglib.demo
 * @Desc:
 * @date 2018/7/3 11:31
 */
public class ProxyFactory implements MethodInterceptor {
    private static Enhancer enhancer = new Enhancer();

    static Callback[] callbacks = new Callback[]{new ProxyFactory(), NoOp.INSTANCE};

    public static Object getInstance(Class clz){
        enhancer.setSuperclass(clz);
        //单个Callback
//        enhancer.setCallback(new ProxyFactory());
        //多个Callback
        enhancer.setCallbacks(callbacks);

        //实现过滤方法调用：不同的方法使用不同的回调,需要Callback数组，MyCallBackFilter返回数组下标索引
        enhancer.setCallbackFilter(new MyCallBackFilter());
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("do something before :"+method.getName());
        methodProxy.invokeSuper(o,objects);
        System.out.println("do something after :"+method.getName());
        return null;
    }
}
