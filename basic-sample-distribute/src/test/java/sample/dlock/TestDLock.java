package sample.dlock;

import org.junit.Test;
import sample.dlock.redis.RedisDLock;

/**
 * @author soyona
 * @Package sample.dlock
 * @Desc:
 * @date 2018/7/24 10:32
 */
public class TestDLock {

    @Test
    public void testLock(){
        DLock lock = new RedisDLock();
        while (!lock.tryLock()){
            System.out.println("其他线程以获取锁。");
        }
        System.out.println("执行业务代码....");
        lock.release();
    }
}
