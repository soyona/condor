package sample.instance;

/**
 * @author soyona
 * @Package sample.instance
 * @Desc:
 * @date 2018/8/1 15:23
 */
public class Parent {
    public static String staticField = "父类--静态变量";
    public String name = "父类--普通变量";

    static {
        System.out.println(staticField);
        System.out.println("父类--静态块");
    }

    {
        System.out.println(name);
        System.out.println("父类--普通块");
    }

    public Parent() {
        System.out.println("父类--构造器");
    }
}
