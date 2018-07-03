package sample.dp.proxy.cglib.mixin;

import net.sf.cglib.proxy.Mixin;

/**
 * @author soyona
 * @Package sample.dp.proxy.cglib.mixin
 * @Desc:
 * @date 2018/7/3 12:54
 */
public class MixinDemo {
    public static void main(String[] args) {

        Object o = Mixin.create(new Class[]{ReadInterface.class,SingInterface.class},new Object[]{new MyReadService(),new MySingService()});
        ReadInterface ri = (ReadInterface)o;
        ri.read();

        SingInterface si = (SingInterface)o;
        si.sing();
    }
}
