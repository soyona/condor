package sample.thread.join;

import java.util.concurrent.TimeUnit;

/**
 * while (isAlive()) {// Thread 类的方法：public final native boolean isAlive(); 如果this对象，这里指该Thread对象，活着，
        wait(0);//Object.wait(),  放弃锁，当前对象是this，是指Thread对象实例，当前线程是Main线程，也就是说，main线程放弃对象锁，等待！
   }

   Main线程 何时被唤醒：
 ##########################################/jdk7/hotspot/src/os/linux/vm/os_linux.cpp#######################################
    基于 JVM Thread cpp实现：

 int ret = pthread_create(&tid, &attr, (void* (*)(void*)) java_start, thread);

 static void *java_start(Thread *thread) {
 ...
 thread->run();
 return 0;
 }
 ##########################################/jdk7/hotspot/src/share/vm/runtime/thread.cpp#######################################

 void JavaThread::run() {
 ...
 thread_main_inner();
 }

 void JavaThread::thread_main_inner() {
 ...
 this->exit(false);
 delete this;
 }

 void JavaThread::exit(bool destroy_vm, ExitType exit_type) {
 ...
 // Notify waiters on thread object. This has to be done after exit() is called
 // on the thread (if the thread is the last thread in a daemon ThreadGroup the
 // group should have the destroyed bit set before waiters are notified).
 ensure_join(this);
 ...
 }

 static void ensure_join(JavaThread* thread) {
 // We do not need to grap the Threads_lock, since we are operating on ourself.
 Handle threadObj(thread, thread->threadObj());
 assert(threadObj.not_null(), "java thread object must exist");
 ObjectLocker lock(threadObj, thread);
 // Ignore pending exception (ThreadDeath), since we are exiting anyway
 thread->clear_pending_exception();
 // Thread is exiting. So set thread_status field in  java.lang.Thread class to TERMINATED.
 java_lang_Thread::set_thread_status(threadObj(), java_lang_Thread::TERMINATED);
 // Clear the native thread instance - this makes isAlive return false and allows the join()
 // to complete once we've done the notify_all below
 java_lang_Thread::set_thread(threadObj(), NULL);

 //=======================================================================================
 lock.notify_all(thread);
 // Ignore pending exception (ThreadDeath), since we are exiting anyway
 thread->clear_pending_exception();
 }


 *
 * Created by kanglei on 09/10/2017.
 */
public class ThreadMethodOfJoin {
    static int count = 0;
    public static void main(String[] args){
        Thread t = new Thread(){
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    count++;
                    System.out.println(Thread.currentThread().getName()+":"+count);
                }
            }
        };
        t.start();
        try {
            t.join();// t这个线程对象只要活着，那么Main线程就等待
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName()+":"+count);
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println(t.getName()+","+t.getState());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
