# 1. ZK快速选主
> [源码分析FastLeader选举算法](https://blog.csdn.net/xhh198781/article/details/6612908)
> [源码分析FastLeader选举算法](https://blog.csdn.net/xhh198781/article/details/6619203)

## 1.1 什么时候快速选主
    服务器初始化时
    服务器失去与leader的通信时
## 1.2 快速选主流程
### 1.2.1 主要信息
   version：版本信息，重写equals的时候使用 
   id：被推荐的leader的sid也就是myid 
   zxid：被推荐的leader的事务id 
   electionEpoch：逻辑时钟，用来判断多个投票是否在同一个选举周期中，该值在服务端是一个自增序列，每次进入新的一轮投票后，都会对该值进行加1操作 
   peerEpoch：被推荐leader的epoch 
   state：当前服务器的状态
#### 服务器初始化时流程(3台)
    服务器1启动时,投票vote(id,zxid)=vote(1,0);
    服务器2启动时,投票vote(id,zxid)=vote(2,0);

    选举原则: zxid 为0,相同,此时比较 id,谁大选谁;

    服务器1中,判断,zxid 为0,相同,此时比较 id,因此 选服务器2;       将选票发给 其他服务器
    服务器2中,判断,zxid 为0,相同,此时比较 id,因此 选服务器2;选自己; 将选票发给 其他服务器

    统计投票:如果有超过半数的机器有相同的投票，那么就可以选举出leader;
    三台服务器:有2台 选 服务器2,因此 服务器2 为leader;

    确定leader,所有服务器改变自己的状态,更新leader信息
#### 服务器运行期间快速选举流程
    leader挂掉后,follower状态变为 looking,而后 进入选举流程;
    每台服务器选举自己为leader,投票vote(id,zxid),并将选票发送到其他服务器
    剩余步骤同上!

# ZK应用
> [官网应用解释](http://zookeeper.apache.org/doc/r3.3.2/recipes.html)
# ZK应用-IBM
> [ZK应用-IBM](https://www.ibm.com/developerworks/cn/opensource/os-cn-zookeeper/)
