package sample.reflect.api;

import java.lang.reflect.Array;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc:
 * @date 2018/5/4 12:29
 */
public class ClassMethodOfGetTypeName {
    public static void main(String[] args) {
        System.out.println(Book.class.getTypeName());
        System.out.println(int.class.getTypeName());
        System.out.println(byte.class.getTypeName());
        System.out.println(Array.class.getTypeName());
        System.out.println(String.class.getTypeName());
        System.out.println((new String[]{}).getClass().getTypeName());
    }
}
