package sample.dlock.zookeeper;

import sample.dlock.DLock;

import java.util.concurrent.TimeUnit;

/**
 * @author soyona
 * @Package sample.dlock.zookeeper
 * @Desc:
 * @date 2018/7/24 10:28
 */
public class ZKDLock implements DLock{
    @Override
    public void lock() {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long timeout, TimeUnit unit) {
        return false;
    }

    @Override
    public void release() {

    }
}
