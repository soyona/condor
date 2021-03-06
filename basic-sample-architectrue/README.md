# 1.架构演进
> [相似历程](https://www.jianshu.com/p/0b6e2c920014)

## 1.1 单一架构应用
> 商品、库存、价格、订单、支付在一个应用，一个DB，一个War包部署
## 1.2 垂直应用架构
> 流量增大，扩展节点称为瓶颈
 
> 拆分业务系统：商品系统、库存系统、价格系统、订单系统

> 各业务后台：商品后台、库存管理后台、价格管理后台、订单后台等
  
> 各系统之间通过Http+Json交互，API剧增
 
> 日志分离，排查问题麻烦-需要各业务系统拽取log跟踪

## 1.3 分布式服务架构
> 前后端分离
 
> 业务系统拆分至20多个，系统交互剧增，
 
> 采用RPC框架，系统交互本地化，依赖Service， 自带Retry机制
 
> 针对高负载业务系统扩展集群，JVM内存配置差异化，Tomcat线程池配置差异化
 
> 业务系统粒度太大，存在不常用服务，高负载服务（库存查询、更新），需要对高负载服务提高稳定性，单独部署，单独优化
## 1.3.1 出现的问题：
> 服务调用Http超时，各系统HttpClient不一致
 
> 各系统Http API规范不一致
  
> 事务不一致，补偿繁琐；例如：订单支付成功后，增加积分，发货通知等事务存在失败可能。  
  
> 业务流程图 梳理服务依赖
 
> 创建订单事务：需要库存日志，促销日志，日志查看，各系统手工排查
 
> 一个业务系统宕机，该系统所有服务不可用
 
> 负载：F5，单点
 
> 与SAP JCO 交互问题，异构系统，需要异构通信中间件 端（HTTP/Dubbo/RMI/WS）到端（JCO）

## 1.3.2 我们需要解决的问题：
> 负载问题，去单点，去中心化，
 
> 支持异构系统通信
 
> 服务依赖可视化，而非流程图
 
> 细粒度服务监控统计，可视化
 
> 服务需自带注册、发现、健康检查、负载均衡等特性；
 
> 开发人员培训，易理解
 
> 可以步步为营，而非一蹴而就，也许不伦不类的架构才适合我们业务，我们的团队 
    
## 1.4 更细粒度拆分
> 业务系统粒度太大，其内部还可再分 核心服务/非核心服务，非核心服务可以采用小集群，核心服务可采用大集群，提高集群资源。
 
> 需要在业务系统基础上进一步拆分：
 
>> 基础服务 例如：生成UniqueID服务、发送消息服务、短信服务、邮件服务
 
>> 核心服务 例如：单品页商品库存价格服务，生成订单服务，会员注册服务等

> 出现服务多层级依赖，需要事务监控，全局事务ID，跟踪日志，全链路日志，熔断，限流

> 需要服务监控
