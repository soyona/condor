一、网络层级模型
===


OSI七层网络模型
---
```text
7、应用层
 
6、表示层
 
5、会话层
 
4、传输层-TCP/UDP
 
3、网络层-IP/ICMP/RIP/IGMP
  
2、数据链路层-ARP/RARP/IEEE802.3/PPP/CSMA/CD
 
1、物理层
```
TCP/IP四层网络模型
---
```text
5、应用层-HTTP/TELNET/FTP/TFTP/DNS/SMTP
 
4、传输层-TCP/UDP
 
3、网络层-IP/ICMP/RIP/IGMP
 
2、数据链路层-ARP/RARP/IEEE802.3/PPP/CSMA/CD
 
1、物理层
```

二、TCP的连接和终止
===

```text
参考：[UNIX网络编程 卷1：套接字联网API(第3版)] P31~P38
```

TCP三次握手状态图
---
```text
[TCP三次握手](https://github.com/soyona/condor/blob/master/basic-sample-socket/src/main/resources/TCP_3_handshake.png)
 
参考：https://blog.csdn.net/zhengleiguo/article/details/37509861
```

TCP四次挥手
---
```text

```

浏览器DNS缓存
---
###### Chrome浏览器查看
```text
chrome://net-internals/#dns
```
本地DNS缓存
---
```text
Windows：
 
查看DNS缓存：
cmd->ipconfig /displaydns
清除DNS缓存：
cmd->ipconfig /flushdns
```


一次Request/Response的完整过程
---
```text
1、浏览器输入域名：http://www.shopin.net, 
2、域名解析、获取IP：
    浏览器从缓存（浏览器本身缓存）中查找DNS缓存
        如果浏览器缓存没有，从本地DNS缓存查找
            如果本地DNS缓存没有，
```

BIO
---
```text
1、阻塞、同步
2、服务器实现：一个连接 一个线程
3、使用场景：
   适用于连接数目比较小且固定的架构；
```

NIO
---
```text
1、非阻塞、同步
2、服务器实现：一个请求 一个线程；连接注册到多路复用器，单线程轮训是否有请求到达，如果到达则开启一个线程进行处理
3、缺点：
    轮询空耗费CPU资源；
    当并发猛增，造成大量线程的创建，同样面临BIO情况
4、使用场景：
   用于连接数目多且连接比较短的架构，比如IM服务

```

AIO
---
```text
1、异步、非阻塞
2、服务器实现：一个有效请求 一个线程
3、使用场景：
用于连接数目多且连接比较长的架构，比如相册服务、
     
```

高性能I/O设计两种模式
---
```text
1、Reactor-用于同步I/O
2、Proactor-用于异步I/O
```

三、Linux处理高并发之I/O多路复用
===

######
```text
参考：https://segmentfault.com/a/1190000003063859
```

 
###### I/O多路复用-select
```java
int select (int n, fd_set *readfds, fd_set *writefds, fd_set *exceptfds, struct timeval *timeout);
```
```text
缺点：
 
1、select支持的文件描述符数量太小，默认1024
2、每次调用select都需要把fd集合从用户态拷贝到内核态，当fd增多时开销会很大
3、每次调用select都需要在内核遍历所有fd，查看有么有就绪的fd，当fd增多时，效率随fd数据增加而线性下降
```

###### I/O多路复用-poll
```text
缺点：
 
1、fd数量过大出现性能下降；
2、poll同样需要遍历所有fd获取就绪的socket
 
```

###### I/O多路复用-epoll
```text
epoll描述：
 

```
```java
/**
* 创建一个epoll的句柄，参数size并不是限制了epoll所能监听的描述符最大个数，只是对内核初始分配内部数据结构的一个建议。 
*/
int epoll_create(int size)；
 
/**
* 对指定文件描述符(fd)的操作(op)
* @epfd： epoll句柄，是epoll_create()的返回值。
* @op：表示op操作，用三个宏来表示：添加EPOLL_CTL_ADD，删除EPOLL_CTL_DEL，修改EPOLL_CTL_MOD。分别添加、删除和修改对fd的监听事件。
* @fd：是需要监听的fd（文件描述符）
* @epoll_event：是告诉内核需要监听什么事
*/
int epoll_ctl(int epfd, int op, int fd, struct epoll_event *event)；
 
/**
* 等待epfd上的io事件，最多返回maxevents个事件。
* 函数返回需要处理的事件数目，等待epfd的io事件，最多返回maxevents个事件
* @events：用来从内核得到事件的集合
* @maxevents：告之内核这个events有多大，这个maxevents的值不能大于创建epoll_create()时的size
* @timeout：参数timeout是超时时间（毫秒，0会立即返回，-1将永久阻塞直到有监听的io事件发生）
* @return：该函数返回需要处理的事件数目，如返回0表示已超时
*/
int epoll_wait(int epfd, struct epoll_event * events, int maxevents, int timeout);
```

```text
epoll工作模式：ET/LT
    
1、LT(level Trigger) 默认模式:
    当epoll_wait检测到描述符事件发生并将此事件通知应用程序，应用程序可以不立即处理该事件。下次调用epoll_wait时，会再次响应应用程序并通知此事件。
2、ET(edge Trigger):
    当epoll_wait检测到描述符事件发生并将此事件通知应用程序，应用程序必须立即处理该事件。如果不处理，下次调用epoll_wait时，不会再次响应应用程序并通知此事件。



```
```text
优点：
1、对于一个进程能够打开的文件数限制，epoll没有限制。（1G内存的服务器大约10万）；
2、IO效率不随fd数目增加而下降；
3、使用mmap加速内核与用户空间的消息传递；
    3.1、epoll通过内核与用户空间mmap同一块内存实现
    3.2、mmap底层是使用红黑树+队列实现，每次需要fd，先查找红黑树，然后放入队列，用户收到epoll_wait消息以后只需要查看消息队列有没有数据
4、

```

###### select/poll/epoll 比较
```text
select、poll、epoll本质上都是同步，读写自己负责，读写是阻塞
```
```text
1、最大支持连接数：
 
    1.1、select 1024(x86)/2048(x64)；
    1.2、poll 无限制
    1.3、epoll 无限制，支持一个进程打开最大数据的socket描述符。这个最大数目（远大于2048）在 /proc/sys/fs/file-max查看，这个配置与系统内存关系很大；
 
2、fd拷贝：
 
    2.1、select每次调用都需要拷贝fd集合;
    2.2、poll每次调用都需要拷贝fd集合;
    2.3、epoll调用epoll_ctl时拷贝仅内核并由内核保存，之后每次epoll_wait不拷贝;
 
3、I/O效率：
 
    3.1、select，每次调用进行线性遍历，时间复杂度O(N)；
    3.2、poll，每次调用进行线性遍历，时间复杂度O(N)；
    3.3、epoll，使用"事件"通知方式，每当fd就绪，系统就会调用注册回调函数，将就绪fd放到rdllist，epoll_wait返回时就拿到就绪的fd。内核实现的epoll会根据每个fd上面的callback函数实现的，这样，只有活跃的socket才会主动的调用callback函数。时间复杂度 O(1)
 
4、进程状态切换：
 
    select、poll每次不断轮询所有fd集合，直到设备就绪，等待期间进程多次进入读/写等待队列，发生多次睡眠和唤醒。epoll需要轮训"就绪链表" 而不是所有fd，
```


I/O模型
===
```text
参考：[UNIX网络编程 卷1：套接字联网API(第3版)] P123
```
```text
一个输入操作通常包括两个不同的阶段：
（1）等待数据准备好
（2）从内核向进程复制数据
对于一个套接字上的输入操作：
第一步：通常涉及等待数据从网络中到达。当所等待分组到达时，它被复制到内核中的某个缓冲区。
第二步：把数据从内核缓冲区复制到应用进程缓冲区。
```
6.2.1 阻塞式I/O模型
---

 ![Mou logo](https://segmentfault.com/img/bVm1c3)
```text
阻塞式I/O(blocking I/O)模型
 
进程调用recvfrom，其系统调用直到数据报达到且被复制到应用进程的缓存区中或者发生错误才返回。
进程在从调用recvfrom开始到他返回的整段时间内是被阻塞的。
recvfrom函数成功返回后，应用进程开始处理数据报。    

```
6.2.2 非阻塞式I/O模型
---
```text

```
6.2.3 I/O复用模型
---
```text

```

6.2.4 信号驱动I/O模型
---
```text

```

6.2.5 异步I/O模型
---
```text

```

```text
同步和异步
 
同步I/O：读写需要用户进程本身读写，数据需经过 内核 到 用户空间 过程。
异步I/O：用户进程无需自身负责读写操作，数据 从 内核 到 用户空间 的复制通过 异步I/O本身执行
```
