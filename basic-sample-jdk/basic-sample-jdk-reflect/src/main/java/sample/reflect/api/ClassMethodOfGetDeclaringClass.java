package sample.reflect.api;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc: 目标在哪个类中声明
 * @date 2018/5/4 11:52
 */
public class ClassMethodOfGetDeclaringClass {
    public static void main(String[] args) {
        System.out.println(Book.class.getDeclaringClass());
    }
}
