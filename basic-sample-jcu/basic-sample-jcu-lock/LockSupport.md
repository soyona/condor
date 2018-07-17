# LockSupport Understanding
## 1.1 LockSupport's key methods
> park()
```text
    public static void park() {
        UNSAFE.park(false, 0L);
    }
```
> park(Object blocker)
```text
    public static void park(Object blocker) {
        Thread t = Thread.currentThread();
        setBlocker(t, blocker);
        UNSAFE.park(false, 0L);
        setBlocker(t, null);
    }

```
> unpark(Thread thread)
```text
    public static void unpark(Thread thread) {
        if (thread != null)
            UNSAFE.unpark(thread);
    }
```

## 1.2 park & unpark
> [Reference](http://www.importnew.com/20428.html)
 
> park 和 unpark 调用是无序的。不像wait/notify/notifyAll调用，线程A在notify线程B之前，线程B必须在wait
 
> park 和 unpark 真正解耦了线程之间的同步，线程之间不需要一个Object来存储状态，而是每个线程的底层实现中都包含一个Park实例
> Parker中有个变量_counter（许可），通过Posix的mutex，condition来实现（待研究）
