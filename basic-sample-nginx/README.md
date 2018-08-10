# 一、Nginx介绍


## 官网
> *From https://www.nginx.com/resources/wiki/*

>NGINX is a free, open-source, high-performance HTTP server and reverse proxy, as well as an IMAP/POP3 proxy server.
NGINX is known for its high performance, stability, rich feature set, simple configuration, and low resource consumption.

>NGINX is one of a handful of servers written to address the C10K problem. Unlike traditional servers, NGINX doesn’t rely on threads to handle requests. **<font color=#00ffff> Instead it uses a much more scalable event-driven (asynchronous) architecture.</font>**  This architecture uses small, but more importantly,
predictable amounts of memory under load. Even if you don’t expect to handle thousands of simultaneous requests, you can still benefit from NGINX’s high-performance and small memory footprint. NGINX scales in all directions: from the smallest VPS all the way up to large clusters of servers.


>摘自：[深入理解Nginx模块开发与架构解析]

```text
Nginx是一个高性能的HTTP和反向代理服务器，也是一个IMAP/POP3/SMTP代理服务器.
Nginx是一款轻量级的Web服务器/反向代理服务器以及电子邮件服务器，特点：占有内存少，并发能力强！
Linux下，Nginx采用epoll事件模型，因此效率相当高。
```
## 为什么选择Nginx
> 摘自：[深入理解Nginx模块开发与架构解析] P5

1. 更快
2. 高扩展性
3. 高可靠性
>Nginx的高可靠性来自于其核心框架代码的优秀设计、模块设计的简单性；另外，官方提供的常用模块都非常稳定，每个worker进程相对独立，master进程在1个worker进程出错时可以快速“拉起”新的worker子进程提供服务。
4. 低内存消耗
>一般情况下，10 000个非活跃的HTTP Keep-Alive连接在Nginx中仅消耗2.5M的内存，这是Nginx支持高并发连接的基础。
5. 单机支持10万以上的并发连接
>理论上，Nginx支持的并发连接上限取决于内存，10万远未封顶。
6. 热部署
>master管理进程与worker工作进程的分离设计，使得Nginx能够提供热部署功能，即可以在7&times;24小时不间断服务的前提下，升级Nginx的可执行文件，当然，它也支持不停止服务就更新配置项、更换日志文件等功能。
7. 最自由的BSD许可协议


# 二、Nginx反向代理
- 代理服务器

```text
一般是指局域网内部的机器通过代理服务器发送请求到互联网上的服务器，代理服务器一般作用在客户端。
```

- 反向代理

```text
客户端向反向代理的命名空间中的内容发送普通请求，接着反向代理服务器将判断转交请求，并将获取的内容返回客户端。
对于客户端来说，访问的是"原始服务器"，对代理服务器无感知。
```
- Nginx VS Apache

```text
相同点：
1、都是HTTP服务器软件
2、功能上都采用模块化结构设计
3、都支持通用的语言接口，如PHP、Perl、Python等
4、支持正向、反向代理、虚拟主机、URL重写、压缩传输、SSL加密传输

不同点：
1、内存使用：Apache处理速度很慢，占用内存资源多
2、编译：Apache所有模块支持动静态编译，Nginx模块都是静态编译
3、I/O模型：Apache不支持epoll，Nginx支持epoll
```



# 三、Nginx负载均衡
> *From http://nginx.org/en/docs/http/load_balancing.html#nginx_load_balancing_methods*

## 负载均衡配置示例
---
```text
http {
    upstream sso.shopin.net{
        server 192.168.200.140:5080;
        server 192.168.200.141:5080;
    }
    server {
        listen 80;
        server_name localhost;
        location /sso {
            proxy_pass        http://sso.shopin.net;
            proxy_set_header   Host             $host;
            proxy_set_header   X-Real-IP        $remote_addr;
            proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;
        }

    }

}
```

### 策略一：轮训

```text
每个请求按时间顺序逐一分配到不同的后端服务器，后端服务器Down掉，能自动剔除
```
```text
upstream backserver {
    server 192.168.200.140;
    server 192.168.200.141;
}
```

### 策略二：指定权重

```text
指定轮询几率，weight和访问比率成正比，用于后端服务器性能不均的情况。
```
```text
upstream backserver {
    server 192.168.200.140 weight=2; # 20%
    server 192.168.200.141 weight=8; # 80%
}
```

### 策略三：IP绑定 ip_hash
```text
每个请求按照访问ip的hash结果分配，这样每个访客固定访问一个后端服务器，可以解决session的问题
```
```text
upstream backserver {
    ip_hash;
    server 192.168.200.140;
    server 192.168.200.141;
}
```
### 策略四：fair（扩展）
> 源码：https://github.com/gnosek/nginx-upstream-fair/tree/master
> 介绍：https://www.nginx.com/resources/wiki/modules/fair_balancer/
```text
按后服务器的相应时间来分配请求，时间短的优先分配。
```
```text
upstream backserver {
    server 192.168.200.140;
    server 192.168.200.141;
    fair;
}
```
### 策略五：url_hash（扩展）
```text
按照访问url的hash结果来分配请求，使每个url定向到同一个后端服务器（缓存时比较有效）
```
```text
upstream backserver {
    server 192.168.200.140;
    server 192.168.200.141;
    hash $request_uri;
    hash_method crc32;
}
```

### 策略六：一致性哈希
>介绍：https://www.nginx.com/resources/wiki/modules/consistent_hash/
>源码：https://github.com/replay/ngx_http_consistent_hash
```text
upstream somestream {
  consistent_hash $request_uri;
  server 10.50.1.3:11211;
  server 10.50.1.4:11211;
  server 10.50.1.5:11211;
}
```
