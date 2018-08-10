package sample.reflect.api;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc: 指定方法名称，方法参数列表 获取public 方法，包括父类
 * @date 2018/5/3 17:05
 */
public class ClassMethodOfGetMethod {
    public static void main(String[] args) {
        //1.public String getName()
        exe("getName");

        //2.public String getName()
        exe("getName",String.class);

        //3.private void setName
        exe("setName",String.class);


        //4.public void pMethod()
        exe("pMethod");
    }



    /**
     * 执行 getField
     * @param name
     */
    public static void exe(String name,Class<?>... parameterTypes){
        try {
            Method method = Book.class.getMethod(name,parameterTypes);
            System.out.println(name+" 方法 =>"+(method==null?"空":method.toGenericString()));
        } catch (NoSuchMethodException e) {
            System.out.println("/(ㄒoㄒ)/~~"+name+" 方法 =>"+e.getCause());
//            e.printStackTrace();
        }
    }
}
