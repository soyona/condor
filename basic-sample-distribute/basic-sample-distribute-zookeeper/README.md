# 1. Zookeeper概述
> [Apache Zookeeper](http://zookeeper.apache.org/)
 
> [参考作者](http://www.cnblogs.com/sunddenly/p/4033574.html)  
```text
Zookeeper是开源的分布式协调服务。在分布式系统中，用来协调多个进程之间同步访问资源，进而有序访问。
在多系统（多进程）访问同一资源时，产生竞争，为了保证资源的安全访问，我们一个资源之外的某个角色来控制多进程的有序访问。
该角色可称为 "话事人"，"协调器"等，分布式锁也可承担次角色。通过锁的排他性、互斥性保证资源被有序访问。Zookeeper可提供分布式锁服务。
```
```text
Zookeeper在CAP方面的优势，使其可以用在大型的分布式架构中，这取决于其Zab协议。
```
# 2. Zookeeper数据模型(Znode)
```text
ZK数据模型类似目录树结构，树的每个节点称为Znode。
```
# 2.1 Znode
## 2.1.1 Znode结构
```text
Znode既可以是文件也可以是目录。其包括三部分内容：
```
- stat：此为状态信息, 描述该Znode的版本, 权限等信息
- data：与该Znode关联的数据
- children：该Znode下的子节点
## 2.1.2 Znode存储数据限制
```text
Znode存储数据大小限制为1M
```
## 2.1.3 Znode访问
```text
原子性读写
```
## 2.1.4 Znode类型
- 临时节点：依赖会话，会话结束节点删除；另：临时节点不允许有子节点
- 永久节点：不依赖会话，除非显示删除
- 顺序临时：节点路径末尾添加10位计数器
- 顺序永久：节点路径末尾添加10位计数器
## 2.1.5 节点监视器
```text
watch机制，监控节点的增删改操作，ZK通知客户端后监视失效，需再次监视。
```
## 2.1.6 节点操作
- create
- delete
- exists
- getACL/setACL
- getChildren
- getData/setData
- sync  客户端Znode视图与ZK服务器同步
# 3. Zookeeper时间概念
## 3.1 Zxid
```text
Znode状态的每一次更新都对应产生一个Zxid的时间戳（64位数字），该时间戳全局有序；每个Znode维护三个时间戳：cZxid（节点创建时），mZxid（节点修改时），pZxid
```
```text
Zxid：64位，高32位表示Leader关系是否改变，低32位递增计数。
```
## 3.2 版本号
```text
每个节点维护三个版本号：
- version 节点数据版本号；
- cversion 子节点版本号；
- aversion 子节点所拥有的ACL版本号；
```
# 3. Zookeeper触发器
```text
watch 事件是一次性触发器；由客户端维护，需要不断注册触发器。
watch 可分为 data watch（getData/exists）和children watch(getChildren)。
```

# 3. Zookeeper协议
# 4. Zookeeper集群
## 4.1 集群角色
### 4.1.1 Leader
### 4.1.2 Follower
### 4.1.3 Observer
## 4.2 集群节点通信
## 4.3 选主


# 5. Zookeeper使用场景
## 5.1 配置管理中心
```text
实现动态配置，即时生效，不用重启服务
```
> 架构设计
![配置中心](../../basic-sample-zookeeper/src/main/resources/zookeeper-config-sample.png)
 
> [案例](https://blog.csdn.net/u011320740/article/details/78742625)
```text

```
## 5.2 服务注册中心
## 5.3 分布式锁
```text

```