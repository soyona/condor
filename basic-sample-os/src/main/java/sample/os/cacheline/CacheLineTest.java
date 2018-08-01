package sample.os.cacheline;

/**
 * @author soyona
 * @Package sample.os.cacheline
 * @Desc:
 * @date 2018/8/1 09:00
 */
public class CacheLineTest {
    public static  final long RUN_TIMES = 500L * 1000L * 1000L;
    static class ThreadC extends Thread{
        ValueSupport vs;
        public ThreadC(ValueSupport valueSupport){
            this.vs = valueSupport;
        }
        @Override
        public void run() {
//            long begin = System.nanoTime();
            for (int i = 0; i < RUN_TIMES ; i++) {
               vs.getValue();
            }
//            System.out.println("耗时："+ (System.nanoTime() - begin));

        }
    }

    public static void singleThreadWithout64(){
        long begin = System.nanoTime();
        new ThreadC(new VariableWithout64Bytes()).start();
        System.out.println("耗时："+ (System.nanoTime() - begin));
    }

    public static void singleThreadWith64(){
        long begin = System.nanoTime();
        new ThreadC(new VariableWith64Bytes()).start();
        System.out.println("耗时："+ (System.nanoTime() - begin));
    }
    public static void multiThreadWithout64() throws InterruptedException {
        VariableWithout64Bytes vw = new VariableWithout64Bytes();
        ThreadC t1 =  new ThreadC(vw);
        ThreadC t2 =  new ThreadC(vw);
        long begin = System.nanoTime();
        t1.start();t2.start();
        t1.join();t2.join();
        System.out.println("耗时："+ (System.nanoTime() - begin));
    }
    public static void multiThreadWith64() throws InterruptedException {
        VariableWith64Bytes vw = new VariableWith64Bytes();
        ThreadC t1 =  new ThreadC(vw);
        ThreadC t2 =  new ThreadC(vw);
        long begin = System.nanoTime();
        t1.start();t2.start();
        t1.join();t2.join();
        System.out.println("耗时："+ (System.nanoTime() - begin));
    }


    public static void main(String[] args) throws InterruptedException {
//        singleThreadWithout64();
//        singleThreadWith64();

        multiThreadWithout64();
        multiThreadWith64();
    }
}
