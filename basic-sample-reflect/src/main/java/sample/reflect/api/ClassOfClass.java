package sample.reflect.api;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc: 列举所有Class 对象
 * @date 2018/5/3 17:33
 */
public class ClassOfClass {
    public static void main(String[] args) {
        exe(byte.class);
        exe(boolean.class);
        exe(short.class);
        exe(int.class);
        exe(long.class);
        exe(float.class);
        exe(double.class);
        exe(Object.class);
        exe(Void.class);
        exe((new ArrayList()).getClass());
        exe((new HashMap()).getClass());
        exe(Map.class);
        exe(Book.class);
    }

    /**
     * 打印class
     * @param clazz
     */
    public static void exe(Class clazz){
        System.out.println(clazz.toGenericString());
    }
}
