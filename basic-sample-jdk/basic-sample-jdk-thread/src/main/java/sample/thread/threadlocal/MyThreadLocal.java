package sample.thread.threadlocal;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author soyona
 * @Package sample.thread.threadlocal
 * @Desc:
 * @date 2018/5/15 10:13
 */
public class MyThreadLocal {
    public  final int threadLocalHashCode = nextHashCode();
    private static AtomicInteger nextHashCode =new AtomicInteger();
    private static final int HASH_INCREMENT = 0x61c88647;
    private static int nextHashCode() {
        System.out.println("#========================="+nextHashCode);
        return nextHashCode.getAndAdd(HASH_INCREMENT);
    }


    private final int no = getNo();


    public  int getNo(){
        return new Random().nextInt();
    }
    public static void main(String[] args) {
        MyThreadLocal myThreadLocal = new MyThreadLocal();
//        System.out.println(myThreadLocal.no);
//        System.out.println(myThreadLocal.no);
//        System.out.println(myThreadLocal.no);

    }

}
