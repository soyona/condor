package sample.reflect.api;

import java.lang.reflect.Field;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc: 返回public fields ，包含父类
 * @date 2018/5/4 12:04
 */
public class ClassMethodOfGetFields {
    public static void main(String[] args) {
        for(Field field:Book.class.getFields()){
            System.out.println(field);
        }
    }
}
