package sample.jvm.localvariable;

/**
 * @author soyona
 * @Package sample.jvm.localvariable
 * @Desc:
 * 局部变量Slot可以重用，方法体中定义的变量，其作用域并不一定覆盖整个方法体
垃圾回收日志配置：
配置一：
在IDEA->Run->Edit Configuration->选项 Configuration中->VM options: 配置内容如下：
-verbose:gc
-XX:+PrintGCDetails
-XX:+PrintGCDateStamps
-Xloggc:/Users/kanglei/GitHub/condor/basic-sample-jvm/gc.log
或者简化：
-verbose:gc
-Xloggc:/Users/kanglei/GitHub/condor/basic-sample-jvm/gc.log

或者
 *
 * 配置二，在IDEA中：
-XX:+PrintGC
-XX:+PrintGCDetails
-XX:+PrintGCDateStamps
-Xloggc:/Users/kanglei/GitHub/condor/basic-sample-jvm/gc.log


 * @date 2018/6/1 16:39
 */
public class LocalVariableDemo {
    public static void main(String[] args) {
        testWithGC();
        testWithoutGC();
    }


    /**
     * GC日志 gc.log：
     0.124: [GC (System.gc())  69468K->66056K(251392K), 0.0007735 secs]
     0.125: [Full GC (System.gc())  66056K->346K(251392K), 0.0041768 secs]
     64M被回收
     *
     *
     */
    public static void testWithGC(){
        {
            byte[] bs = new byte[64 * 1024 * 1024];
        }
        //如果 声明a变量，局部变量表Slot会被重用，那么gc会引发垃圾回收
        int a=0;
        System.gc();
    }

    /**
     * GC日志：gc.log
     0.135: [GC (System.gc())  65882K->65882K(251392K), 0.0003951 secs]
     0.136: [Full GC (System.gc())  65882K->65862K(251392K), 0.0111741 secs]
     64M没有被回收
     *
     */
    public static void testWithoutGC(){
        {
            byte[] bs = new byte[64 * 1024 * 1024];
        }
        System.gc();
    }
}
