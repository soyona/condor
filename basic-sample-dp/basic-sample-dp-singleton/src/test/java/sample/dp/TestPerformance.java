package sample.dp;

import org.junit.Test;
import sample.dp.singleton.SingletonB;
import sample.dp.singleton.SingletonC;
import sample.dp.singleton.SingletonF;

/**
 * @author soyona
 * @Package sample.dp
 * @Desc:
 * @date 2018/8/16 17:29
 */
public class TestPerformance {
    @Test
    public void testF(){
        long b = System.currentTimeMillis();
        for(int i=0;i<1000000;i++){
            SingletonC.getInstance();
//            System.out.println(SingletonC.getInstance());
        }
        System.out.println(System.currentTimeMillis() -b);
    }
    @Test
    public void testC(){
        long b = System.currentTimeMillis();
        for(int i=0;i<1000000;i++){
            SingletonC.getInstance();
//            System.out.println(SingletonC.getInstance());
        }
        System.out.println(System.currentTimeMillis() -b);
    }

    @Test
    public void testB(){
        long b = System.currentTimeMillis();
        for(int i=0;i<1000000;i++){
            SingletonB.getInstance();
//            System.out.println(SingletonC.getInstance());
        }
        System.out.println(System.currentTimeMillis() -b);
    }

    /**
     *
     *
     *
     * Unsafe.allocateInstance 性能较差
     */
    @Test
    public void testUnsafe(){
        long b = System.currentTimeMillis();
        for(int i=0;i<1000000;i++){
            try {
                SingletonF.getU().allocateInstance(SingletonF.class);
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
//            System.out.println(SingletonC.getInstance());
        }
        System.out.println(System.currentTimeMillis() -b);
    }
}
