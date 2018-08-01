package sample.os.cacheline;
import java.lang.instrument.Instrumentation;
/**
 * @author soyona
 * @Package sample.os.cacheline
 * @Desc:
 * @date 2018/8/1 08:38
 */
public class ObjectSize {
//    private static Instrumentation instrumentation;
//
//    public static void premain(String args, Instrumentation inst) {
//        instrumentation = inst;
//    }
//
//    public static long getObjectSize(Object o) {
//        return instrumentation.getObjectSize(o);
//    }
public static void main(String[] args) throws IllegalAccessException {
    final ClassIntrospector ci = new ClassIntrospector();
    ObjectInfo res = ci.introspect( new VariableWith64Bytes() );
    System.out.println( res.getDeepSize() );

    ObjectInfo res1 = ci.introspect( new VariableWithout64Bytes() );
    System.out.println( res1.getDeepSize() );
}
}
