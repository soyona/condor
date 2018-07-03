package sample.dp.proxy.cglib.demo;

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

/**
 * @author soyona
 * @Package sample.dp.proxy.cglib.demo
 * @Desc:
 * @date 2018/7/3 12:21
 */
public class MyCallBackFilter implements CallbackFilter {
    @Override
    public int accept(Method method) {
        if(method.getName().equals("say")){
            System.out.println("调用 say()");
            return 0;
        }else {
            return 0;
        }
    }
}
