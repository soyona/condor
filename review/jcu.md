# 多线程

## 1.volatile 可以保证线程安全吗？
    不可以；
    参考：https://www.cnblogs.com/dolphin0520/p/3920373.html
    回答此问题涉及多个知识点：
    1）解释内存模型
    2）解释CPU三级及主存关系
    3）解决CPU缓存不一致问题（总线加锁、缓存一致性协议（MESI））
        3.1）缓存一致性协议：它核心的思想是：当CPU写数据时，如果发现操作的变量是共享变量，即在其他CPU中也存在该变量的副本，会发出信号通知其他CPU将该变量的缓存行置为无效状态，因此当其他CPU需要读取这个变量时，发现自己缓存中缓存该变量的缓存行是无效的，那么它就会从内存重新读取。
    4）并发概念
        4.1）原子性：
        4.2）可见性：多个CPU，其中一个CPU更新缓存，但没有同步到主存，此时出现可见性问题；
        4.3）顺序性：指令重排，前提是结果正确，否则不会指令重排，如果遇到多线程指令重排会引起问题，如果保证并发执行的正确性，必须保证顺序性；
    5）JMM
        JMM定义变量的访问规则；
        JMM规定所有变量都是存在主存中（物理内存），每个线程都有自己的工作内存（CPU缓存/高速缓存）；
        线程对变量的操作都必须在工作内存中进行，不能直接对主存操作；
        工作内存相互独立；
        Java语言如何保证原子性、可见性、顺序性？
        5.1）原子性
            在Java中，对基本类型变量的读取和赋值操作都是原子性；
            举例分析一下是否是原子性操作：
            x=10;//原子性；赋值操作，10赋值给x，在工作内存中；
            y=x; //非原子性；分两步：第一步：从主存中读取x 第二步：将读取的x写到工作内存；两步都是原子性，但是两个原子性合起来 不是原子性；
            x++;//非原子性；读取x，+1，回写工作内存；
            x = x -1;//非原子性
            以上只能保证基本类型变量的读取、赋值是原子性；
            如何要保证更大范围的原子性？
                Synchronized、Lock
        5.2）可见性
            Java提供volatile关键字来保证可见性；
            volatile int count = 0；
            如果count被修改，Java保证count值立即被更新到主存中，当有其他线程需要读取count时，去主存中读取新值；
            但是普通变量不能保证什么时候被写入主存，无法保证可见性；
            synchronized和Lock也能保证可见性，保证同一时刻只有一个线程执行同步代码，释放锁之前会将变量修改刷新到主存中。可以保证可见性；
        5.3）有序性
            JMM中，允许编译器和处理器对指令进行重排，但是不会影响单线程的执行，却会影响到多线程并发执行的正确性。
            Java中，可以通过valotile、synchronized、lock保证有序性；
            Happens-before原则 保证有序性；
            5.3.1）程序次序规则：一个线程内，书写顺序执行；
            5.3.2）锁定规则：一个unLock操作先行发生于后面对同一个锁额lock操作；
            5.3.3）valatile变量规则：对一个变量的写操作先行发生于后面对这个变量的读操作？？？？？
            5.3.4）传递规则：A先B,B先C => A先C
            5.3.5）线程启动规则：Thread对象的start()方法先行发生于此线程的每个一个动作
            5.3.6）线程中断规则：对线程interrupt()方法的调用先行发生于被中断线程的代码检测到中断事件的发生
            5.3.7）线程终结规则：线程中所有的操作都先行发生于线程的终止检测，我们可以通过Thread.join()方法结束、Thread.isAlive()的返回值手段检测到线程已经终止执行
            5.3.8）对象终结规则：一个对象的初始化完成先行发生于他的finalize()方法的开始
    6）volatile关键字
        该关键字两层语义：
        6.1）保证多个线程的可见性；//立即写入主存
        6.2）禁止指令重排；
            举例：
            x = 2;        //语句1
            y = 0;        //语句2
            volatile flag = true;  //语句3
            x = 4;         //语句4
            y = -1;       //语句5 
            volatile关键字能保证，执行到语句3时，语句1和语句2必定是执行完毕了的，且语句1和语句2的执行结果对语句3、语句4、语句5是可见的。
        6.3）不保证原子性；
            i++;//包括三个操作：读、+1、写入工作内存；

        如何保证并发i++原子性：
        synchronized/Lock/CAS(硬件指令CMPXCHG)/AtomicInteger       
    7）volatile的原理和实现机制
         加入volatile关键字生成汇编代码多出lock前缀指令，实际上相当于一个内存屏障，其3个功能：
         7.1）确保后面的指令不会被排到内存屏障之前的位置，前面的指令不会被排到内存屏障后面的位置；内存屏障指令执行时，表示前面的指令已经全部完成；
         7.2）强制将兑缓存的修改立即写入主存；
         7.3）如果是写操作，将导致其他CPU中对应的缓存行失效；
    8）volatile适用场景
        8.1）状态标记
        8.2）double-check
        8.3）原子类CAS
## 2. Synchronized关键字的作用，性能如何优化？
    作用：控制多线程访问共享资源；
    涉及多个知识点：
    2.1）三种用法
        2.1.1）静态方法：类对象
        2.1.2）普通方法：实例对象
        2.1.3）代码块：实例对象：monitorenter、monitorexit
    2.2）ObjectMonitor
    2.3) ObjectWaiter
    2.4) 
## 3. JVM 锁优化
    3.1）适应性自旋：
        忙循环/自旋次数，根据“长、短“ 采取不同策略等待
    3.2）锁消除
        根据”逃逸分析“，代码不存在竞争，就不需要加锁。比如StringBuffer.append()
    3.3）锁粗化
        如果一系列操作都对同一个对象反复加锁解锁，就会把锁的范围扩大，避免频繁加锁解锁操作
    3.4）轻量级锁
        作用：在没有多线程竞争的前提下，减少传统的重量级锁使用的操作系统互斥量产生的性能损耗。
        对象头中Mark Word结构：
            对象哈希码、对象分代年龄；
            指向锁记录的指针；
            指向重量级锁的指针；
            空；
            偏向线程ID、偏向时间戳、分代年龄
        轻量级锁加锁过程：
            >首先线程栈中创建锁记录空间，用来存放对象头MarkWord的拷贝；
            >CAS修改对象头MarkWord更新为栈帧中锁记录的指针，如果更新成功，表示拥有锁，并且对象MarkWord的标志位转变为00
            >如果CAS更新失败，判断当前对象头MarkWord是否指向当前线程栈帧，如果不是，表示有其他线程抢占，如果有两条以上的线程竞争同一锁，那轻量级锁不再有效，膨胀为重量级锁；锁标志转为10，MarkWord中存储的就是指向重量级锁的指针。
            
    3.5）偏向锁
        作用：在无竞争的情况下把整个同步都消除掉，连CAS都不做；
        偏向第一个获得他的线程，以后该线程不再同步；使用CAS修改MarkWord的线程ID。