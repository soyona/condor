package sample.dlock;

import java.util.concurrent.TimeUnit;

/**
 * @author soyona
 * @Package sample.dlock
 * @Desc:
 * @date 2018/7/24 10:27
 */
public interface DLock {
    public void lock();
    public boolean tryLock();

    /**
     * 超时版本加锁
     * @param timeout 超时时间
     * @param unit unit
     * @return
     */
    public boolean tryLock(long timeout, TimeUnit unit);
    public void release();
}
