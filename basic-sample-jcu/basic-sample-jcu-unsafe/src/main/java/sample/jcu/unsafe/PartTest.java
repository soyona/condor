package sample.jcu.unsafe;

import org.junit.Test;

import java.util.concurrent.locks.LockSupport;

import static org.junit.Assert.assertTrue;

/**
 * @author soyona
 * @Package sample.jcu.unsafe
 * @Desc: Unsafe.park/unpark
 * @date 2018/7/14 15:51
 */
public class PartTest {
    @Test
    public void parkTest() throws InterruptedException {
        final boolean[] flag = new boolean[1];

        Thread t = new Thread(){
            @Override
            public void run() {
                System.out.println("park before");
                UnsafeUtils.unsafe.park(true,20000000L);
                System.out.println("park after");
                flag[0] = true;
            }
        };
//        t.start();
//        UnsafeUtils.unsafe.unpark(t);
//        t.join(1000L);
        UnsafeUtils.unsafe.park(false,20000000L);
        assertTrue(flag[0]);
    }

}
