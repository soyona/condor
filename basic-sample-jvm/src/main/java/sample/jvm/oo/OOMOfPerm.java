package sample.jvm.oo;

import javassist.CannotCompileException;
import javassist.ClassPool;

/**
 * @author soyona
 * @Package sample.jvm.oo
 * @Desc:
 * JVM 启动参数：-Xmx100M -XX:MaxPermSize=10M -XX:+UseParallelGC
 * JDK 7
 * @date 2018/6/29 12:39
 */
public class OOMOfPerm {
    static ClassPool cp = ClassPool.getDefault();
    public static void main(String[] args) throws Exception {
        javassist();
    }
    public static void javassist() throws CannotCompileException {
        for (int i = 0; ; i++) {
            Class c = cp.makeClass("smpale.jvm.oo" + i).toClass();
        }
    }
}
