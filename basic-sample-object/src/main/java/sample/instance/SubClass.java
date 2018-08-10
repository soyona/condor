package sample.instance;

/**
 * @author soyona
 * @Package sample.instance
 * @Desc:
 * @date 2018/8/1 15:24
 */
public class SubClass extends Parent {
    public static String staticField = "子类--静态变量";
    public String name = "子类--普通变量";
    static {
        System.out.println(staticField);
        System.out.println("子类--静态块");
    }

    {
        System.out.println(name);
        System.out.println("子类--普通块");
    }

    public SubClass() {
        System.out.println("子类--构造器");
    }
}
