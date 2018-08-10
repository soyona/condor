package sample.thread;

import java.lang.management.ManagementFactory;

/**
 * Created by soyona on 13/10/2017.
 */
public class Utils {
    public static String pid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(name);
        return name.split("@")[0];
    }
}
