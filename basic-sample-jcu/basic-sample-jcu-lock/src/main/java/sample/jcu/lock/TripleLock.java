package sample.jcu.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author soyona
 * @Package sample.jcu.lock
 * @Desc: 同步工具，允许三个线程并行访问，超过限制，其他线程阻塞
 * @date 2018/7/11 16:11
 */
public class TripleLock implements Lock{
    private final Sync sync = new Sync(3);
    //自实现同步器，代理实现 lock 和 release
    static class Sync extends AbstractQueuedSynchronizer{
        public Sync(int count){
            super();
            setState(count);
        }

        @Override
        protected int tryAcquireShared(int reduce) {
            for(;;){
                int c0 = getState();
                int c1 = getState() - reduce;
                // 如果 c1 >= 0 ,才进行CAS，
                if(c1 < 0 || compareAndSetState(c0,c1)){
                    return c1;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int add) {
            for(;;){
                int c0 = getState();
                int c1 = c0 + add;
                if(compareAndSetState(c0,c1)){
                    return true;
                }
            }
        }
    }
    @Override
    public void lock() {
        sync.acquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquireShared(1)>0;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(1,unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }


}
