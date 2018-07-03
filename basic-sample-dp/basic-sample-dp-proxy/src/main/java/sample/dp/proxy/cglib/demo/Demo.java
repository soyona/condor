package sample.dp.proxy.cglib.demo;

/**
 * @author soyona
 * @Package sample.dp.proxy.cglib.demo
 * @Desc:
 * @date 2018/7/3 11:51
 */
public class Demo {
    public static void main(String[] args) {
        DemoService demoService = (DemoService) ProxyFactory.getInstance(DemoService.class);
        demoService.say();
    }
}
