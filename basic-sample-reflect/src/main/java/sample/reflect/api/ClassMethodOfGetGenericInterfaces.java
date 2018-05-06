package sample.reflect.api;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc: 返回参数化类型的接口
 * @date 2018/5/4 11:38
 */
public class ClassMethodOfGetGenericInterfaces {
    public static void main(String[] args) {
        for (Type type:Book.class.getGenericInterfaces()) {
            System.out.println(type);
        }


        for (Type type:HashMap.class.getGenericInterfaces()) {
            System.out.println(type);
        }
    }
}
