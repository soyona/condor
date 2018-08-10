# 1. TCP/IP四层网络模型

```text
5、应用层-HTTP/TELNET/FTP/TFTP/DNS/SMTP
 
4、传输层-TCP/UDP
 
3、网络层-IP/ICMP/RIP/IGMP
 
2、数据链路层-ARP/RARP/IEEE802.3/PPP/CSMA/CD
 
1、物理层
```

# 2.TCP的连接和终止
> [UNIX网络编程 卷1：套接字联网API(第3版) P31~P38]()
 
> [三次四次](https://blog.csdn.net/qq_16634723/article/details/80428645) 
 
> [参考](http://www.cnblogs.com/newwy/p/3234536.html)

## 2.1 TCP三次握手状态图

```text
[TCP三次握手](https://github.com/soyona/condor/blob/master/basic-sample-socket/src/main/resources/TCP_3_handshake.png)
 
参考：https://blog.csdn.net/zhengleiguo/article/details/37509861
```

## 2.2 TCP四次挥手

```text

```
## 3.TCP流量控制
> [流量控制](https://www.cnblogs.com/newwy/p/3254029.html)
```text
TCP通过滑动窗口机制来控制流量，接收方在返回ACK中会包含所接收的窗口大小，以控制发送方的数据发送。如果接收方返回超时或中断，发送方采取计时重发。

```
## 4.TCP的拥塞控制
> [拥塞控制](https://blog.csdn.net/SEU_Calvin/article/details/53198282)
 
> 四种算法： 
```text
慢开始、
拥塞避免、
快重传、
快恢复、
```
### 4.1 拥塞控制和流量控制的区别
```text
拥塞控制是防止过多的数据注入网络，可以使网络中的路由器或链路不致过载，是一个全局性的过程。
 ，是一个端到端的问题，主要就是抑制发送端发送数据的速率，以便接收端来得及接收。
```
### 4.1.1 TCP的拥塞机制是什么？
> [腾讯面试题 ](https://blog.csdn.net/shuxnhs/article/details/80644531)
```text
TCP的拥塞控制机制是什么？请简单说说。 
答：我们知道TCP通过一个定时器（timer）采样了RTT并计算RTO，但是，如果网络上的延时突然增加，那么，TCP对这个事做出的应对只有重传数据，然而重传会导致网络的负担更重，于是会导致更大的延迟以及更多的丢包，这就导致了恶性循环，最终形成“网络风暴” —— TCP的拥塞控制机制就是用于应对这种情况。 
首先需要了解一个概念，为了在发送端调节所要发送的数据量，定义了一个“拥塞窗口”（Congestion Window），在发送数据时，将拥塞窗口的大小与接收端ack的窗口大小做比较，取较小者作为发送数据量的上限。 
拥塞控制主要是四个算法： 
1.慢启动：意思是刚刚加入网络的连接，一点一点地提速，不要一上来就把路占满。 
连接建好的开始先初始化cwnd = 1，表明可以传一个MSS大小的数据。 
每当收到一个ACK，cwnd++; 呈线性上升 
每当过了一个RTT，cwnd = cwnd*2; 呈指数让升 
阈值ssthresh（slow start threshold），是一个上限，当cwnd >= ssthresh时，就会进入“拥塞避免算法” 
2.拥塞避免：当拥塞窗口 cwnd 达到一个阈值时，窗口大小不再呈指数上升，而是以线性上升，避免增长过快导致网络拥塞。 
每当收到一个ACK，cwnd = cwnd + 1/cwnd 
每当过了一个RTT，cwnd = cwnd + 1 
拥塞发生：当发生丢包进行数据包重传时，表示网络已经拥塞。分两种情况进行处理： 
等到RTO超时，重传数据包 
sshthresh = cwnd /2 
cwnd 重置为 1 
3.进入慢启动过程 
在收到3个duplicate ACK时就开启重传，而不用等到RTO超时 
sshthresh = cwnd = cwnd /2 
进入快速恢复算法——Fast Recovery 
4.快速恢复：至少收到了3个Duplicated Acks，说明网络也不那么糟糕，可以快速恢复。 
cwnd = sshthresh + 3 * MSS （3的意思是确认有3个数据包被收到了） 
重传Duplicated ACKs指定的数据包 
如果再收到 duplicated Acks，那么cwnd = cwnd +1 
如果收到了新的Ack，那么，cwnd = sshthresh ，然后就进入了拥塞避免的算法了。
```
## 5.TCP可靠传输的实现
> [TCP可靠传输的实现](http://www.cnblogs.com/newwy/p/3238362.html)