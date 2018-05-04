package sample.reflect.api;

import java.lang.reflect.Field;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc: 获取在类中声明的所有Fields，包括  public, protected, default, package，private fiedls 访问权限，不包括继承的
 * @date 2018/5/3 09:26
 */
public class ClassMethodOfGetDeclaredFields {


    public static void main(String[] args) {
        Class clazz = Book.class;
        //返回public, protected, default,package的field，不包括父类的
        Field[] fields = clazz.getDeclaredFields();
        System.out.println("---获取该类中声明的Field---");
        for (Field field:fields){
            System.out.println(field.getName());
        }
        System.out.println("---获取该类中声明的Field---");
    }
}
