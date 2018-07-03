package sample.dp.proxy.cglib.mixin;

/**
 * @author soyona
 * @Package sample.dp.proxy.cglib.mixin
 * @Desc:
 * @date 2018/7/3 12:55
 */
public class MyReadService implements ReadInterface {
    @Override
    public void read() {
        System.out.println("I'm reading.");
    }
}
