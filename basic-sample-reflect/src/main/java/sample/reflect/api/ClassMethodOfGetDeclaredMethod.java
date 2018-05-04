package sample.reflect.api;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc: 获取声明的方法指定名称，指定参数类型列表
 * @date 2018/5/4 10:28
 */
public class ClassMethodOfGetDeclaredMethod {
    public static void main(String[] args) {
        //1.public String getName(String parameter)
        exe("getName",String.class);

        //2.private void setAge(int age)
        exe("setAge",int.class);

        //3.Parent.class =>private String pSay(Date date)，父类方法找不到哦
        exe("pSay",Date.class);

    }


    /**
     * 执行getDeclaredField
     * @param name
     */
    public static void exe(String name,Class<?>...paramterTypes){
        try {
            Method method = Book.class.getDeclaredMethod(name,paramterTypes);
            System.out.println(method+" method =>"+(method==null?"空":method.toGenericString()));
        } catch (NoSuchMethodException e) {
            System.out.println("/(ㄒoㄒ)/~~"+" method =>"+e.getCause());
//            e.printStackTrace();
        }
    }
}
