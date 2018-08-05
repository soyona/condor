package sample.dp.singleton;

import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author soyona
 * @Package sample.dp.singleton
 * @Desc:
 * @date 2018/8/5 15:42
 */
public class SingletonF {

    // Unsafe mechanics
    private static final sun.misc.Unsafe U;

    private static Unsafe initU(){
        try {
            Constructor constructor = Unsafe.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            Unsafe unsafe = (Unsafe) constructor.newInstance();
            return unsafe;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    //初始化
    private static final long initCtl_OFFSET;
    static {
        try {
            U = initU();
//            System.out.println(U);
            Class<?> k = SingletonF.class;
//            System.out.println(k.getDeclaredField("initCtl"));
            //获取对象偏移量，不能获取对象实例的属性
//            initCtl_OFFSET = U.objectFieldOffset(k.getDeclaredField("initCtl"));
            /**
             * 获取静态变量，方法区，属性的偏移量
             */
            initCtl_OFFSET = U.staticFieldOffset(k.getDeclaredField("initCtl"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }
    //并发初始化控制
    private static transient volatile int initCtl;
    private static SingletonF instance;
    //私有构造，防止外部new
    private SingletonF(){
        //防止反射击穿，掩耳盗铃，Unsafe创建实例不调用构造，
        if(instance != null){
            throw new RuntimeException();
        }
    };

    public static SingletonF getInstance(){
        int ctl;
        while(instance == null){
            if((ctl = initCtl) < 0 ){
                Thread.yield(); // lost initialization race; just spin
            }else if(U.compareAndSwapInt(SingletonF.class, initCtl_OFFSET, ctl, -1)){
                //CAS 修改为-1
                instance = new SingletonF();
//                System.out.println(instance);
                break;
            }
        }
        return instance;
    }

    public static void main(String[] args) {
//        SingletonF instance = SingletonF.getInstance();
        try {
            System.out.println(SingletonF.U.allocateInstance(SingletonF.class));;
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
