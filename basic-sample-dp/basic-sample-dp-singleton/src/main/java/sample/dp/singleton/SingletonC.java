package sample.dp.singleton;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author soyona
 * @Package sample.dp.singleton
 * @Desc:
 * @date 2018/8/5 16:49
 */
public class SingletonC {
    public static final AtomicInteger ctl = new AtomicInteger(0);
    private static volatile SingletonC instance;
    private SingletonC(){};
    public static SingletonC getInstance(){
        while(instance == null){//确保 工作空间 拿到 对象的堆地址，再终止
            if(ctl.get() == 0){//说明没有其他线程被初始化
                if(ctl.incrementAndGet() == 1){
                    instance = new SingletonC();
                    break;
                }
            }else{
                Thread.yield();
            }
        }
        return instance;
    }


}
