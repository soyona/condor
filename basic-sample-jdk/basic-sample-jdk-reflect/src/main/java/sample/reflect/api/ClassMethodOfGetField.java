package sample.reflect.api;

import java.lang.reflect.Field;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc: 获取指定的Field，包括父类
 * @date 2018/5/3 16:25
 */
public class ClassMethodOfGetField {

    public static void main(String[] args){
        //1.获取public BigDecimal price
        exe("price");

        //2.获取private int age，不能获取
        exe("age");

        //3.获取protected String publishedDate，不能获取
        exe("publishedDate");

        //4.获取父类public String p_name;
        exe("p_name");
    }

    /**
     * 执行 getField
     * @param name
     */
    public static void exe(String name){
        try {
            Field field = Book.class.getField(name);
            System.out.println(name+"=>"+(field==null?"空":field.toGenericString()));
        } catch (NoSuchFieldException e) {
            System.out.println("/(ㄒoㄒ)/~~"+name+"=>"+e.getCause());
//            e.printStackTrace();
        }
    }

}
