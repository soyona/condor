package sample.object;

import java.lang.instrument.Instrumentation;

/**
 * @author soyona
 * @Package sample.object
 * @Desc:
 * @date 2018/8/1 09:31
 */
public class ObjectShallowSize {
    private static Instrumentation inst;

    public static void main(String[] args) {
        System.out.println(sizeOf(new VariableWith64Bytes()));
    }

    public static void premain(String agentArgs, Instrumentation instP) {
        inst = instP;
    }

    public static long sizeOf(Object obj) {
        return inst.getObjectSize(obj);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
