package sample.jcu.synchronization;

/**
 * @author soyona
 * @Package sample.jcu.synchronization
 * @Desc:
 * @date 2018/7/16 13:00
 */
public class SynchronizedUsage {

    public void sing(){
        /**
         *
         */
        synchronized(this){

        }

        synchronized(this.getClass()){

        }
    }

    /**
     * synchronized object's method,the lock of Object
     */
    public synchronized void  say(){

    }

    /**
     * synchronized static method, the lock of class
     */
    public synchronized static void  read(){

    }

}
