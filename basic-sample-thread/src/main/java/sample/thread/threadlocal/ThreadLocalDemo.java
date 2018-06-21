package sample.thread.threadlocal;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 考查点：
 * ThreadLocal类的实现,
 * ThreadLocal.ThreadLocalMap 内部类
 * ThreadLocalMap.Entry 内部类，
 * Thread类的属性： ThreadLocal.ThreadLocalMap threadLocals = null;
 * Created by kanglei on 05/11/2017.
 */
public class ThreadLocalDemo {

    public static void main(String[] args) {
        MyThreadLocal myThreadLocal = new MyThreadLocal();
        MyThreadLocal myThreadLocal2 = new MyThreadLocal();
        ThreadLocal tl = new ThreadLocal();
        ThreadLocal t2 = new ThreadLocal();

        tl.set("山丘");
        /**
         *
         * private void set(ThreadLocal<?> key, Object value)
         *
         */
        System.out.println(tl.get());
        System.out.println(tl.get());
        System.out.println(tl.get());
        System.out.println(tl.get());
        System.out.println("");
        System.out.println(myThreadLocal.threadLocalHashCode);
        System.out.println(myThreadLocal.threadLocalHashCode);
        System.out.println(myThreadLocal.threadLocalHashCode);
    }
}
