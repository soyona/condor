package sample.reflect.api;

import java.lang.reflect.Method;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc: 获取Class中定义所有public的方法对象，包括继承自父类或者父接口中的
 * @date 2018/5/3 16:10
 */
public class ClassMethodOfGetMethods {
    public static void main(String[] args) {
        Method[] methods = Book.class.getMethods();
        for (Method m:methods){
            System.out.println(m.toGenericString());
        }
    }
}
