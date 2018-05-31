package sample.review.integer;

import java.lang.reflect.Field;

/**
 * @author soyona
 * @Package sample.review.integer
 * @Desc: 值传递，值交换如何实现
 * @date 2016/7/3 19:10
 */
public class IntegerSwap {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Integer a = 1,b = 2;
        System.out.println("a="+a+"\nb="+b);
        swap(a,b);
        System.out.println("a="+a+"\nb="+b);
    }

    /*
     * @desc: 通过反射交换值
     * @author: soyona
     * @date: 2016/7/3 20:10
     * @param: [a, b]`
     * @return: void
     */
    public static void swap(Integer a,Integer b) throws NoSuchFieldException, IllegalAccessException {
        Field field = Integer.class.getDeclaredField("value");
        field.setAccessible(true);
        int temp = a;//自动拆箱
        field.setInt(a,b.intValue());
        field.setInt(b,temp);
    }
}
