package sample.reflect.api;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc: 类中定义的public的 成员类 或者 接口
 * @date 2018/5/4 12:00
 */
public class ClassMethodOfGetClasses {
    public static void main(String[] args) {
        for(Class clazz:Book.class.getClasses()){
            System.out.println(clazz.toString());
        }

    }
}
