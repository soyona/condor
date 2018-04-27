package sample.clazz;

import java.util.HashMap;

/**
 * @author soyona
 * @Package sample.reflect
 * @Desc: java.lang.Class 类分析
 * @date 2018/4/27 14:58
 */
public class ClassDemo {
    public static void main(String[] args) {
        Class claz = HashMap.class;
        System.out.println(claz.toGenericString());

        System.out.println(Integer.class.toGenericString());

        System.out.println(Integer.class.isInstance(444));
    }
}
