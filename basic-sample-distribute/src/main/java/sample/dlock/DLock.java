package sample.dlock;

/**
 * @author soyona
 * @Package sample.dlock
 * @Desc:
 * @date 2018/7/24 10:27
 */
public interface DLock {
    public boolean tryLock();
    public void release();
}
