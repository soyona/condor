package sample.jcu.unsafe;

import sun.misc.Unsafe;

/**
 * @author soyona
 * @Package sample.jcu.unsafe
 * @Desc:
 * @date 2018/7/13 11:41
 */
public class UnsafeDemo {
    public static void main(String[] args) {
        AtomicOrder atomicOrder = new AtomicOrder("2019");
        atomicOrder.compareAndSet("2019","2018");
        System.out.println(atomicOrder.getOrderNo());
    }
}
