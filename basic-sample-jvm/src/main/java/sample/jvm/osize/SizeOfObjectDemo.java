package sample.jvm.osize;

import java.lang.instrument.Instrumentation;

/**
 * @author soyona
 * @Package sample.jvm.osize
 * @Desc: 如何计算Object对象大小
 *
 * @date 2018/6/11 10:17
 */
public class SizeOfObjectDemo {
    static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation instP) {
        instrumentation = instP;
    }
    public static void main(String[] args) {
        Object o = new Object();
        System.out.println(instrumentation.getObjectSize(o));
    }
}
