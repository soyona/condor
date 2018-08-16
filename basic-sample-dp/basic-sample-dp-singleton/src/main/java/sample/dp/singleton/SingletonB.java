package sample.dp.singleton;

/**
 * @author soyona
 * @Package sample.dp.singleton
 * @Desc:
 * @date 2018/8/5 16:08
 */
public class SingletonB {
    private static volatile SingletonB instance;
    private SingletonB(){};
    public static SingletonB getInstance(){
        if(null == instance){
            synchronized (SingletonB.class){
                instance = new SingletonB();
                return instance;
            }
        }else {
            return instance;
        }
    }
}
