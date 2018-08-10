package sample.jvm.gc;

/**
 * @author soyona
 * @Package sample.jvm.gc
 * @Desc:
 * @date 2018/6/13 11:19
 */
public class GCDemo {
    public static void main(String[] args) throws InterruptedException {
        testMinorGC();
    }
    public static void testMinorGC() throws InterruptedException {
        for (;;){
            Thread.currentThread().setName("kanglei");
//            Thread.sleep(100);
            byte[] bs = new byte[1 * 10 * 1024];//1MB
            System.out.println("#");
        }
    }
}
