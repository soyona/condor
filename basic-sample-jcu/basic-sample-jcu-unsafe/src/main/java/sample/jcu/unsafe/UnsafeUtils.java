package sample.jcu.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author soyona
 * @Package sample.jcu.unsafe
 * @Desc:
 * @date 2018/7/13 15:45
 */
public class UnsafeUtils {
    public static final Unsafe unsafe;
    private static Unsafe getInstance(){
        try {
            Constructor constructor = Unsafe.class.getDeclaredConstructor();
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
    static {
        unsafe = getInstance();
    }
}
