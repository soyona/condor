# 1.MAC RabbitMQ安装
> [官网](http://www.rabbitmq.com/install-standalone-mac.html)
## 1.1 brew install rabbitmq
> 第一步：终端执行命令下载安装包：brew install rabbitmq

> 第二步：自动下载目录：/Library/Caches/Homebrew/rabbitmq-3.5.2.tar.gz
 
>> 解压：/Users/kanglei/Dev/rabbitmq/rabbitmq_server-3.5.2
 
> 第三步：启动命令：sbin/rabbitmq-server
>> 出错：ERROR: epmd error for host kourais-MacBook-Pro: timeout (timed out)
 
>> 解决：sudo vim /etc/hosts 最后一行添加  127.0.0.1     kourais-MacBook-Pro 
 
>> 再次启动：sbin/rabbitmq-server 成功

> 第四步：启动插件
 
>>  