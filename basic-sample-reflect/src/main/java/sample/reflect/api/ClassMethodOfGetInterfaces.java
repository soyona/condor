package sample.reflect.api;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc:  返回该类实现的接口，或者接口继承的接口，按照声明顺序
 * @date 2018/5/4 11:22
 */
public class ClassMethodOfGetInterfaces {
    public static void main(String[] args) {
        for (Class clazz:Book.class.getInterfaces()) {
            System.out.println(clazz);
        }
    }
}
