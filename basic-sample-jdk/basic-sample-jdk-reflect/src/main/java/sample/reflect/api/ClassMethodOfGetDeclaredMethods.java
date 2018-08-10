package sample.reflect.api;

import java.lang.reflect.Method;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc: 返回所有 public, protected, default, and private methods  ，不包含继承来的
 * @date 2018/5/3 17:57
 */
public class ClassMethodOfGetDeclaredMethods {
    public static void main(String[] args) {
        for (Method method:Book.class.getDeclaredMethods()){
            System.out.println(method.toGenericString());
        }
    }
}
