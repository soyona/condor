package sample.reflect.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc: 根据指定参数类型 返回public 构造对象，不包含父类
 * @date 2018/5/3 17:19
 */
public class ClassMethodOfGetConstructor {
    public static void main(String[] args) {
        //1.public无参构造
        exe();
        //2.private Book(String name)
        exe(String.class);
        //3.public Book(String name, int age, String id)
        exe(String.class,int.class,String.class);
    }


    /**
     * 执行 getConstructor(parameterTypes)
     * @param parameterTypes
     */
    public static void exe(Class<?>... parameterTypes){
        try {
            Constructor constructor = Book.class.getConstructor(parameterTypes);
            System.out.println(constructor+" 构造方法 =>"+(constructor==null?"空":constructor.toGenericString()));
        } catch (NoSuchMethodException e) {
            System.out.println("/(ㄒoㄒ)/~~"+" 构造方法 =>"+e.getCause());
//            e.printStackTrace();
        }
    }
}
