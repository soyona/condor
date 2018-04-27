package sample.collection.hashmap;

import com.sun.deploy.net.proxy.MacOSXSystemProxyHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author soyona
 * @Package sample.collection.hashmap
 * @Desc: HashMap并发问题，丢失元素,模拟线程并发put
 * @date 2016/4/24 10:17
 */
public class HashMapTest {

    public static final int CONCURRENT_NUM=10;

    public static Map map = new HashMap();

    public static List<Thread> threadList = new ArrayList<Thread>();

    //并发控制
    public static CountDownLatch cdl = new CountDownLatch(CONCURRENT_NUM);


    public static void init() throws InterruptedException {
        for (int i = 0; i < CONCURRENT_NUM; i++) {
            Thread t = new Thread(){
                @Override
                public void run() {
                    this.setName("Thread-"+this.getId());
                    try {
                        cdl.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    put();
                }
            };
            threadList.add(t);
            t.start();
            cdl.countDown();
        }
        for (Thread t:threadList) {
            t.join();
        }
    }

    /*
     * @desc:
     * @author: soyona
     * @date: 2016/4/24 10:34
     * @param: []
     * @return: void
     */
    public  static void put(){
        for (int i = 0; i <100 ; i++) {
            map.put(Thread.currentThread().getName()+"-"+i,"$");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        init();
        System.out.println(map.size());
//        Object.hashCode(1);
    }
}
