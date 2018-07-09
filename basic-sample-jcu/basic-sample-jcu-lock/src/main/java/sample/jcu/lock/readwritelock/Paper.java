package sample.jcu.lock.readwritelock;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author soyona
 * @Package sample.jcu.lock.readwritelock
 * @Desc: 运用读写锁，实现Paper的 一个作者写，多个读者 读的例子
 * @date 2018/7/9 20:36
 */
public class Paper {
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private String content;
    public void write(){
        lock.writeLock().lock();
        String segment = "="+ new Random().nextInt()+"$ ";
        content += segment;
        System.out.println("写作内容："+segment);

        lock.writeLock().unlock();
    }
    public void read(){
        lock.readLock().lock();
        System.out.println("读到内容："+content);
        lock.readLock().unlock();
    }
}
