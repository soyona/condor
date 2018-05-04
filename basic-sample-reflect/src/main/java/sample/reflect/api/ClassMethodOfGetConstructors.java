package sample.reflect.api;

import java.lang.reflect.Constructor;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc: 获取Class中定义的public的 构造方法  public constructors，不包含父类中的
 * @date 2018/5/3 15:46
 */
public class ClassMethodOfGetConstructors {
    public static void main(String[] args) {
        Constructor[] constructors = Book.class.getConstructors();
        for (Constructor cons:constructors){
            System.out.println(cons.toGenericString());
        }
    }
}
