package sample.redis;

/**
 * @author soyona
 * @Package sample.redis
 * @Desc:
 * @date 2018/7/24 10:24
 */
public interface DistributeLock {
    public boolean tryLock();
    public void release();
}
