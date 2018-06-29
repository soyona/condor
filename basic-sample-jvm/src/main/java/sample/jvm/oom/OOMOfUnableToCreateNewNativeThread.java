package sample.jvm.oom;

/**
 * @author soyona
 * @Package sample.jvm.oom
 * @Desc:
 * -Xss：默认 count=4074
 * -Xss1M   count=4072
 * -Xss2M   count=4072
 *
 * @date 2018/6/29 15:49
 */
public class OOMOfUnableToCreateNewNativeThread {
    //线程数
    static int count;
    public static void main(String[] args) {
        while(true){
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(++count);
                            for(;;){
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception e){
                                    System.err.println(e);
                                }
                            }
                        }

                    }
            ).start();
        }
    }
}
