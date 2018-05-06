package sample.reflect.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc: 指定构造参数类型列表返回 构造
 * @date 2018/5/4 10:42
 */
public class ClassMethodOfGetDeclaredConstructor {
    public static void main(String[] args) {
        //1.public Book(),无参构造
        exe();
        //2.default Book(String name, int age, String id)
        exe(String.class,int.class,String.class);
        //3.private Book(String name)
        exe(String.class);
    }

    /**
     * 执行getDeclaredConstructor
     * @param paramterTypes
     */
    public static void exe(Class<?>...paramterTypes){
        try {
            Constructor constructor = Book.class.getDeclaredConstructor(paramterTypes);
            System.out.println(constructor+" constructor =>"+(constructor==null?"空":constructor.toGenericString()));
        } catch (NoSuchMethodException e) {
            System.out.println("/(ㄒoㄒ)/~~"+" constructor =>"+e.getCause());
//            e.printStackTrace();
        }
    }
}
