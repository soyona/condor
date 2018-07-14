package sample.jcu.lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author soyona
 * @Package sample.jcu.lock
 * @Desc: 自旋锁实现，代码来自：http://ifeve.com/java_lock_see1/
 * @date 2018/7/12 17:55
 */
public class SpinLock {
    private AtomicReference<Thread> sign =new AtomicReference<>();

    /**
     * AtomicReference 实例属性
     * private volatile V value;
     *
     * private static final long valueOffset;

     通过反射获取属性value，再根据unsafe.objectFieldOffset获取该Field的偏移量
     static {
     try {
     valueOffset = unsafe.objectFieldOffset
     (AtomicReference.class.getDeclaredField("value"));
     } catch (Exception ex) { throw new Error(ex); }
     }
     */
    /**
     *
     * unsafe.compareAndSwapObject(this, valueOffset, expect, update)
     *
     * AtomicReference 实例对象的 偏移量 来获取 线程对象
     *
     */
    public void lock(){
        Thread current = Thread.currentThread();
        //CAS不成功 继续循环，CAS成功 结束循环
        /**
         * Atomically sets the value to the given updated value, if the current value == expected value
         *  public final boolean compareAndSet(V expect, V update) {
                return unsafe.compareAndSwapObject(this, valueOffset, expect, update);
            }
         */
        while(!sign .compareAndSet(null, current)){
        }
    }

    public void unlock(){
        Thread current = Thread.currentThread();
        //CAS不成功 继续循环，CAS成功 结束循环
        while(!sign .compareAndSet(current, null)){
        }
    }

}
