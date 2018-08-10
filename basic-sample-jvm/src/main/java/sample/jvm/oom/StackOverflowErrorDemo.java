package sample.jvm.oom;

/**
 * @author soyona
 * @Package sample.jvm.oom
 * @Desc:
 * JVM启动参数：-Xss2M
 *
 * 默认：   count=10082
 * -Xss1M  count=10046
 *
 * -Xss2M  count=21018
 * @date 2018/6/29 12:56
 */
public class StackOverflowErrorDemo {
    static int count=0;
    public static void main(String[] args) {
        stackOverflow();
    }

    public static  void stackOverflow(){
        System.out.println(++count);
        stackOverflow();
    }

}
