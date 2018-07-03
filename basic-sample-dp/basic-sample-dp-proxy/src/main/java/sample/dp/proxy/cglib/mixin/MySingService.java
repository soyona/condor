package sample.dp.proxy.cglib.mixin;

/**
 * @author soyona
 * @Package sample.dp.proxy.cglib.mixin
 * @Desc:
 * @date 2018/7/3 12:56
 */
public class MySingService implements SingInterface {
    @Override
    public void sing() {
        System.out.println("I'm singing.");
    }
}
