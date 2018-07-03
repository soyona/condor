package sample.jvm.oom;

import javassist.CannotCompileException;
import javassist.ClassPool;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author soyona
 * @Package sample.jvm.oom
 * @Desc:
 * @date 2018/6/29 12:04
 */
public class OOMOfMetaSpace {
    static ClassPool cp = ClassPool.getDefault();
    public static void main(String[] args) throws CannotCompileException {
//        javassist();
        cglib();
    }


    /**
     *
     * @throws CannotCompileException
     *
     * JVM 启动参数： -XX:MaxMetaspaceSize=9M
     *
     * Exception in thread "main" javassist.CannotCompileException: by java.lang.OutOfMemoryError: Metaspace
    at javassist.ClassPool.toClass(ClassPool.java:1085)
    at javassist.ClassPool.toClass(ClassPool.java:1028)
    at javassist.ClassPool.toClass(ClassPool.java:986)
    at javassist.CtClass.toClass(CtClass.java:1079)
    at sample.jvm.oom.OOMOfMetaSpace.javassist(OOMOfMetaSpace.java:26)
    at sample.jvm.oom.OOMOfMetaSpace.main(OOMOfMetaSpace.java:20)
    Caused by: java.lang.OutOfMemoryError: Metaspace
    at java.lang.ClassLoader.defineClass1(Native Method)
    at java.lang.ClassLoader.defineClass(ClassLoader.java:760)
    at java.lang.ClassLoader.defineClass(ClassLoader.java:642)
    at sun.reflect.GeneratedMethodAccessor1.invoke(Unknown Source)
    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke(Method.java:497)
    at javassist.ClassPool.toClass2(ClassPool.java:1098)
    at javassist.ClassPool.toClass(ClassPool.java:1079)
     */
    public static void javassist() throws CannotCompileException {
        for (int i = 0; ; i++) {
            Class c = cp.makeClass("smpale.jvm.oom" + i).toClass();
        }
    }


    public static void cglib(){
        while (true){
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(Object.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    return methodProxy.invoke(o,objects) ;
                }
            });
            Object object = (Object)enhancer.create();
        }
    }


}
