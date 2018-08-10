package sample.dp.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author soyona
 * @Package sample.dp.proxy.jdk
 * @Desc: 处理器
 * @date 2018/6/22 13:53
 */
public class MyInvocationHandler implements InvocationHandler{
    private Object target;
    public MyInvocationHandler(Object target){
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(proxy.getClass());
        System.out.println(method.getName());
        System.out.println("自定义处理器。。");
        System.out.println("method.invoke()---start.可插入自定义逻辑。");
        method.invoke(this.target,args);
        System.out.println("method.invoke()---end.可插入自定义逻辑");
        return null;
    }
}
