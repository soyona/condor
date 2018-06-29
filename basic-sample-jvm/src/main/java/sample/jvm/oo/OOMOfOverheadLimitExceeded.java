package sample.jvm.oo;

import java.util.Map;
import java.util.Random;

/**
 * @author soyona
 * @Package sample.jvm.oo
 * @Desc:
 * JDK8
 * JVM:启动参数：-Xmx12m -XX:+UseParallelGC
 * -XX:+UseConcMarkSweepGC, 或者 -XX:+UseG1GC,
 * @date 2018/6/29 12:48
 */
public class OOMOfOverheadLimitExceeded {
    public static void main(String args[]) throws Exception {
        Map map = System.getProperties();
        Random r = new Random();
        while (true) {
            map.put(r.nextInt(), "value");
        }
    }
}
