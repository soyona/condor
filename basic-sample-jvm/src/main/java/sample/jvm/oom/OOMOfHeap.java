package sample.jvm.oom;

/**
 * @author soyona
 * @Package sample.jvm.oom
 * @Desc:
 * JVM 启动参数，堆最大内存设置：-Xmx50m
 * @date 2018/6/29 12:30
 */
public class OOMOfHeap {

    public static void main(String[] args) {
        //100M 字节,
        byte[] bytes = new byte[100 * 1024 * 1024];
    }
}
