package sample.reflect.api;

import java.lang.reflect.Constructor;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc: 返回所有声明的构造方法，包括 public, protected, default,and private constructors
 * @date 2018/5/3 18:01
 */
public class ClassMethodOfGetDeclaredConstructors {
    public static void main(String[] args) {
        for (Constructor constructor: Book.class.getDeclaredConstructors()) {
            System.out.println(constructor.toGenericString());
        }
    }
}
