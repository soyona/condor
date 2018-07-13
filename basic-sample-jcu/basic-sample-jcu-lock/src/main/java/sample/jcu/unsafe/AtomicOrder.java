package sample.jcu.unsafe;

import sun.misc.Unsafe;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * @author soyona
 * @Package sample.jcu.unsafe
 * @Desc:
 * @date 2018/7/13 11:43
 */
public class AtomicOrder implements Serializable{
    //初始化 权限问题，会检验类加载器的权限，如果开发者使用，采用另外一种方式
    private static final Unsafe unsafe = getUnsafe();
    private static long orderNooffset;
    private String orderNo;
    public AtomicOrder(String orderNo){
        this.orderNo = orderNo;
    }

    /**
     * 自行实现获取Unsafe实例
     * @return
     */
    static Unsafe getUnsafe(){
        try {
            Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafeField.setAccessible(true);
            Unsafe temp = (Unsafe)theUnsafeField.get(null);
            return temp;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    static {
        try {
            Field orderNoField = AtomicOrder.class.getDeclaredField("orderNo");
            //获取存放实例属性orderNo的内存偏移量
            orderNooffset = unsafe.objectFieldOffset(orderNoField);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public final boolean compareAndSet(String expect, String update) {
        return unsafe.compareAndSwapObject(this, orderNooffset, expect, update);
    }

    public String getOrderNo(){
        return this.orderNo;
    }
}
