package sample.reflect.api;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc: 返回类中定义的public, protected, default, and private classes  and interfaces，不包含父类中
 * @date 2018/5/3 17:49
 */
public class ClassMethodOfGetDeclaredClasses {
    public static void main(String[] args) {
       for(Class clazz:Book.class.getDeclaredClasses()) {
           System.out.println(clazz.toGenericString());
       }
    }
}
