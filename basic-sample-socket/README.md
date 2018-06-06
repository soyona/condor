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
######Chrome浏览器查看
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

Linux处理高并发之多路复用
---
######select
```text

 
 
```

######poll
```text

 
```

######epoll
```text


 
```