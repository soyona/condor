# 1. About Thread
## 1.1 Creating a Thread
> Java defines two ways by which a thread can be created
- By implementing the Runnable interface.
- By extending the Thread class
### 1.1.1 Implementing the Runnable interface.
> [kanglei's code](./src/main/java/sample/thread/ThreadDemo.java)
```text
    class MyThread implements Runnable{

        @Override
        public void run() {
            System.out.println("implementing  Runnable interface.");
        }
    }
```
```    
    @Test
    public void excThread(){
        Thread t = new Thread(new MyThread());
        //On calling start(),a new stack is provided to the thread and the run() method is called to introduce the new thread into the programe.
        t.start();
    }
```
### 1.1.2 Extending the Thread class
> [kanglei's code](./src/main/java/sample/thread/ThreadDemo.java)
 
> This is another way to create a thread by a new class that extends **Thread** class,the extending class must override run() method 
which is the entry point of new thread.
```text
    class MyThread2 extends Thread{
        @Override
        public void run() {
            System.out.println("extending Thread class.");
        }
    }
```
```text
    /**
     * How to start a thread that extending the Thread class
     */
    @Test
    public void excThread2(){
        Thread t = new MyThread2();
        t.start();
    }
```
## Note 1: start the thread that implementing Runnable interface.
> If you are implementing Runnable interface in you class,then you need to explicitly create a
Thread class Object and need to pass the Runnable interface implementing class Object as a parameter in its constructor. 

## Note 2: run() or start()
> In above program,if we directly call run() method,withouting using start() method,Doing so ,the thread won't be allocated a new call stack,
 ,and it will start running in the current call stack,that is the call stack of the **main** thread.

## Note 3:
> Can we Start a thread twice?
```text
No,a thread cannot be started twice,if you try to do so,IllegalThreadStateException will be thrown.
When a thread is running state,and you'll try to start again,Exception is thrown.
```
> Thread#start() code:
```text
public synchronized void start() {

        if (threadStatus != 0)//  :>:>:>:>:>:>:>:>:>:>:>state 
            throw new IllegalThreadStateException();
        group.add(this);
        boolean started = false;
        try {
            start0();
            started = true;
        } finally {
            try {
                if (!started) {
                    group.threadStartFailed(this);
                }
            } catch (Throwable ignore) {
                /* do nothing. If start0 threw a Throwable then
                  it will be passed up the call stack */
            }
        }
    }
```




# How to read a thread dump
## Reference 
> https://dzone.com/articles/how-to-read-a-thread-dump
