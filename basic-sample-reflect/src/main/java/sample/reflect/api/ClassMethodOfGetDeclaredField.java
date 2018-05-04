package sample.reflect.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc: 获取指定声明的Field，无父类
 * @date 2018/5/4 10:22
 */
public class ClassMethodOfGetDeclaredField {
    public static void main(String[] args) {
        //1.private int age;
        exe("age");
        //2.private String id;
        exe("id");
        //3.public BigDecimal price;
        exe("price");
        //4.Parent.class public String p_name; 父类field 找不到
        exe("p_name");
    }

    /**
     * 执行getDeclaredField
     * @param name
     */
    public static void exe(String name){
        try {
            Field field = Book.class.getDeclaredField(name);
            System.out.println(field+" Field =>"+(field==null?"空":field.toGenericString()));
        } catch (NoSuchFieldException e) {
            System.out.println("/(ㄒoㄒ)/~~"+" Field =>"+e.getCause());
//            e.printStackTrace();
        }
    }
}
