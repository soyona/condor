# Mysql

## 1.函数（索引列）可以使用索引吗？
    1.1) MYSQL索引类型：
        normal：普通索引
        unique：唯一索引
        full text：全文索引
        单列、多列：
        最左前缀：多列索引
    1.2）MYSQL 索引失效的场景：
        只对<,<=,=,>,>=,between,in使用索引，like 中不以%开头的情况
        1.2.1) where != 
        1.2.2) where day(col1), where字句条件用函数
        1.2.3）join中，主键和外检的数据类型相同时才使用
        1.2.4）在order by中，MYSQL只有在排序条件下不是一个查询条件表达式的情况下使用
        1.2.5）数据列中有许多重复值，索引不会有好效果
        1.2.6）条件中 or 不适用，除非or 前后列都加索引
        1.2.7）字符串列，必须在查询条件中 使用 引号，否则不适用
        1.2.8）like 以%开头 不适用
        1.2.9）索引列进行计算 where age-1=8 ,索引失效
        1.2.10）where rount(id)=10，函数 索引失效
    1.3）索引考虑
        
        where 和 join使用的列
    1.4）Hash索引
        缺点：
            1.4.1）不能使用范围查询，仅仅满足 = in <=>,因为是计算后的hash值
            1.4.2) 无法避免数据排序，hash前后的顺序不一定一直
            1.4.3）Hash索引不能利用部分索引键查询，对于组合索引。
            1.4.4）如果遇到大量Hash值相等情况，性能不一定比B-Tree高
    1.5）B-Tree索引
        参考：https://www.jianshu.com/p/486a514b0ded
        1.5.1)B+Tree
            B-Tree树的变种，只有leaf node存储数据，其他节点存储key；
            Mysql中的B+Tree在经典的B+Tree基础上进行了优化，增加了顺序访问指针，在每个叶子节点增加了一个指向相邻叶子节点的指针，形成了带有顺序访问指针的B+Tree。
            优点：
                提高了区间访问性能：如果要查找key为从18到49的访问数据，当涨到18后，只需要顺着指针顺序遍历就可以一次性访问到所有的数据节点，极大的提高了区间查询效率，无需返回上层父节点重复遍历查找，减少IO次数。
            磁盘存取原理：
                索引以文件形式存储在磁盘上，需要IO操作，耗费巨大。磁盘的IO寻址最耗时。
                磁盘的寻址是指：将数据逻辑地址 按照 寻址逻辑将地址翻译成物理地址，确定磁道、扇区。此过程需要寻道/寻道时间；
            局部性原理与磁盘预读：
                磁盘和主存以页（逻辑块-4K）为单位交换数据， 磁盘每次是预读，预读的数据大小是页的整数倍。
                若程序读取的数据不在主存，会触发缺页异常，此时系统向磁盘发出读盘信号，磁盘找到数据起始位置向后读取1页或者几页载入内存。
        1.5.2）B-/+Tree性能衡量指标：磁盘I/O次数
            1. mysql结合系统存储结构优化处理，磁盘结构和磁盘预读；
            2. B+Tree单个节点能放多个子节点，相同IO次数
            3. B+Tree只在叶子节点存储数据/叶子节点包含指针。利用索引快速定位数据索引范围，先定位索引在通过索引高效快速定位数据。
            4. Mysql设计利用了磁盘预读原理 
                将一个B+Tree节点大小设为一个页的大小，在新建节点时申请一个页的大小空间，这样就能保证一个节点物理上存储在一个页里，加之计算机存储分配都是按照页对齐的，这样实现一个节点只需一次IO操作。
                B-Tree、B+Tree单个节点能放多个子节点，查询IO次数相同（mysql查询io次数最多3-5次，所以需要每个节点需要存储很多数据）
            5. B+Tree更适合外存索引，和节点出度d有关，d越大索引性能越好，因为B+Tree内节点去掉了Data区，因此可以拥有更大的出度，拥有更好的性能。
        1.5.3）B+Tree 在MyISAM和InnoDB 中底层实现不同
            1. MyISAM采用非聚簇索引
                myi索引文件 和 myd 数据 文件分离，索引文件又保存数据记录的指针。
                叶子节点data域存储指向数据记录的指针地址。
                存储结构：frm-表定义、myi-myisam索引、myd-myisam数据
            2. InnoDB采用 聚簇索引
                采用聚簇索引- InnoDB数据&索引文件为一个idb文件，表数据文件本身就是主索引，相邻的索引临近存储。 叶节点data域保存了完整的数据记录(数据[除主键id外其他列data]+主索引[索引key:表主键id])。 
                叶子节点直接存储数据记录，以主键id为key,叶子节点中直接存储数据记录。(底层存储结构: frm -表定义、 ibd: innoDB数据&索引文件)

## 2.索引的左侧原则？
    2.1）联合索引：索引可以建立在多列上，
        联合索引的最左原则就是建立索引KEY union_index (a,b,c)时，等于建立了(a)、(a,b)、(a,b,c)三个索引，从形式上看就是索引向左侧聚集，所以叫做最左原则，因此最常用的条件应该放到联合索引的组左侧。
        利用联合索引加速查询时，联合查询条件符合“交换律”，也就是where a = 1 and b = 1 等价于 where b = 1 and a = 1，这两种写法都能利用索引KEY union_index (a,b,c)。

## 3.三大日志的作用binlog、undolog、redolog
    3.1) binlog
        Mysql Server层的记录日志，用于记录所有更新且提交了数据或者已经潜在更新提交了数据的所有语句，以”事件“的形式保存，它描述数据更改。
        作用：
            用于恢复；
            记录所有发送给从服务器的语句；
    3.2) Undolog
        3.2.1）作用：
            为了实现事务的原子性；
            辅助完成事务持久化；
            InnoDB引擎用来实现MVCC；
        3.2.2）原理：
            为了满足原子性，操作任何数据之前，先将数据备份到一个地方（Undolo）->修改数据，如果出现错误或者用户执行rollback，系统可以利用Undolog中的备份数据恢复事务开始之前的状态。
        3.2.3）缺陷
            每个事务提交前将数据和Undolog写入磁盘，这样导致大量的磁盘IO，因此性能很低。
            如果数据能够将数据缓存一段时间，就能够较少IO，提高性能；
    3.3) Redolog
        Innodb存储引擎层的日志，记录的是新数据的备份。事务提交前，只要将RedoLog持久化即可，不需要将数据持久化，当系统崩溃时，虽然数据没有持久化，但是RedoLog已经持久化，系统就可以根据Redolog内容将所有数据恢复到最新的状态。
## 4.Mysql 5.5、5.6、5.7在主从同步上做了哪些改进
    4.1）Mysql复制架构演进史：
        > MySQL 3.23.15   Replication,准后实时同步方式，复制由两个线程，Master中一个，Slave一个线程：负责I/O和Sql，无relay log，
            这种模式缺点：读取event时间得速度受限于apply速度；较大延迟时，导致大量bin log没有备份到Slave
        > MySQL 4.0.2 Slave端 读取event的I/O和执行SQL的线程独立，同时引入relay log；
            I/O线程读取event事件后写入relay log，SQL线程从relay log读取event执行SQL，即使SQL执行慢，不会影响I/O线程，Master的bin log会尽可能的同步到Slave。当Master宕机不会出现大量数据丢失；
        > MySQL 5.5之前采用异步复制方式：主库的事务执行不关心从库的备份进度，如果备库延迟，主库，将导致数据丢失。于是5.5 引入半同步复制，主库在应答Client提交事务之前至少保证一个从库接收并写入relay log。
        
        > MySQL 5.6版本引入并发复制（schema级别），基于schema级别的并发复制核心思想：“不同schema下的表并发提交时的数据不会相互影响，即slave节点可以用对relay log中不同的schema各分配一个类似SQL功能的线程，来重放relay log中主库已经提交的事务，保持数据与主库一致”。可见MySQL5.6版本的并发复制，一个schema分配一个类似SQL线程的功能。
        
        > MYSQL 5.7.17，引入：InnoDB Group Replication，全同步方式
        
    4.2）MySQL三种复制方式
        4.2.1) asynchronous 异步复制
            主库线程把binlog事件写入binlog后，主库只会通知dump线程发送binlog日志而后继续处理提交操作，此时并不会保证binlog传到任何一个从库节点上；
            流程：
            1）正常的复制为：事务一（t1）写入binlog buffer；dumper线程通知slave有新的事务t1；binlog buffer进行checkpoint；slave的io线程接收到t1并写入到自己的的relay log；slave的sql线程写入到本地数据库。 这时，master和slave都能看到这条新的事务，即使master挂了，slave可以提升为新的master。

            2）异常的复制为：事务一（t1）写入binlog buffer；dumper线程通知slave有新的事务t1；binlog buffer进行checkpoint；slave因为网络不稳定，一直没有收到t1；master挂掉，slave提升为新的master，t1丢失。

            3）很大的问题是：主机和从机事务更新的不同步，就算是没有网络或者其他系统的异常，当业务并发上来时，slave因为要顺序执行master批量事务，导致很大的延迟。

        4.2.2) Semisynchronous 半同步复制
            介于异步和全同步之间，主库只需要等待至少一个从库节点收到并且Flush Binlog到Relay Log文件即可，主库无需等待从库反馈。同时，这里只是一个收到的反馈，而不是已经完全执行并且提交的反馈。
            特点：
            1. Dumper线程任务是发送通知和接收slave的ack；
            2. Master的事务会阻塞等待，等待会超时，超时后关闭版同步复制，转换为异步复制。
            3. 从库只有在接收到主库的某一事务的所有binlog，并且写入并fulsh到relaylog文件之后，才会通知主库的dumper线程，
            4. 数据不一致的问题，由于master等待从回馈ACK导致
            5. 5.7版本对半同步复制的改进是 无损复制 loss-less semi-Synchronous模式（AFTER_SYNC）：在binlog sync之后，在engine层commit之前 等待Slave ACK，如此可以利用Group commit，有利于性能提升。
            半同步复制（AFTER_COMMIT 5.6默认）和无损复制（AFTER_SYNC 5.7默认）：
                区别一：时间点不同
                    - 半同步复制是innodb层commit log后等待ack，主从切换有丢失数据风险；
                    - 无损复制是在innodb层收到write binlog之后，等待ack，主从切换会有数据变多风险。
                区别二：数据不一致性
                    - 半同步复制意味着在Master节点上，这个刚刚提交的事物对数据库的修改，对其他事物是可见的。因此，如果在等待Slave ACK的时候crash了，那么会对其他事务出现幻读，数据丢失。
                    - 无损复制在write binlog完成后，就传输binlog，但还没有去写commit log，意味着当前这个事物对数据库的修改，其他事物也是不可见的。因此，不会出现幻读，数据丢失风险。因此5.7引入了无损复制（after_sync）模式，带来的主要收益是解决after_commit导致的master crash后数据丢失问题，因此在引入after_sync模式后，所有提交的数据已经都被复制，故障切换时数据一致性将得到提升。
                区别三：性能提升，支持发送binlog和接收ack异步化
                    旧semi sync受限于dump thread，因为dump thread承担了传送binlog给slave和等待接收slave反馈任务，且串行，dump thread必须等待slave返回之后才会传送下一个events事务。dump thread 已然成为整个半同步提高性能的瓶颈。高并发时，影响TPS。
                    5.7的semi sync，独立出一个Ack Receiver线程，专门用于接收Slave返回的ack请求，将之前发送和接收工作分为两个线程来处理，可并行处理。极大提高性能。5.7.17版本前，Ack Receiver线程采用select机制监听slave返回，由于select 1024fd限制会导致宕机。因此5.7.17版本后，采用poll机制，poll与select没有本质区别，只是句柄没有上限，都是基于链表来存储；缺点：大量的fd需要在用户态和内核态之间切换，再者，poll的水平触发使得第一次没有处理的fd会在下次再次报告。
                区别四：性能提升，控制主库接收slave写事务成功反馈数量
                    
                    MySQL 5.7新增了rpl_semi_sync_master_wait_slave_count参数，可以控制主库接收多少个slave写事务成功反馈。
                区别五：性能提升，Binlog互斥锁改进

                    - 旧版本半同步复制在提交binlog的写会话和dump thread读binlog的操作都会对binlog添加互斥锁，导致binlog文件的读写是串行化的，并发性能极低；
                    - MYSQL5.7 对binlog lock进行了两方面优化
                        - 移除dump thread对binlog的互斥锁；
                        - 加入安全边际保证binlog的读安全；
        4.2.3) fully synchronous 全同步复制
            主库提交事务后，所有从库节点都必须收到Apply并且提交这些事务，然后主库线程才能继续做后续操作；
            缺点：主库完成一个事务的时间被拉长，性能降低。
    4.3）MYSQL并行复制的改进
        并行复制涉及 生产者-消费者模式，任务队列
        参考：https://www.kancloud.cn/thinkphp/mysql-parallel-applier/45908
        4.3.1）单线程复制
        4.3.2）5.6 支持并行复制，引入GTID
            基于schema，也就是基于库的，如果用户的MYSQL数据库实例中存在多个schema，对于从机复制速度的确可以有大幅度提升；
        4.3.3）5.7 真正的并行复制
            基于组提交，Slave服务器的回放与主机是一致的。一组提交的事务可以并行回放，因为这些事务已经进入prepare阶段。
            GTID支持基于组的并行复制，通过GTID识别哪个组；
    4.4）各版本变化：
        主从同步：主执行提交之后，将语句记录到binlog，
        从 启动一个IO线程从主传输到本地，进入本地的relaylog；
        从 另启一个SQL线程负责顺序执行relaylog中的语句，
        问题：IO性能损耗；从机数据延迟；主宕机有脏数据；
        5.5版本： 加入半同步复制
            作用：优化了复制脏数据存在；
            原理：
        5.6版本： 加入GTID（全局事务ID），加入了多线程复制和组提交的新模式
            5.6.1）GTID实际上是由UUID+TID组成，
                UUID是MYSQL实例的唯一标识；
                TID标识该实例上已经提交的事务数量，且递增；
                GTID的目的：
                    判断事务发生在哪个实例上；
                    方便Replication的Failover，M宕机的情况下，能迅速根据GTID找到同步停止点。
            5.6.2）多线程并发复制
                针对多库的情况，开启多线程，每个库有独立的IO线程，可以交叉并发传输。提高多库业务的复制速度。
            5.6.3）组提交
                提升了binlog和innodb的redolog的落盘效率；
                多个事务同时执行完成，数据需要持久化到log中时，会全部进入待提交队列。最先进入队列的事务线程成为leader线程，其后进入的为follower线程，leader线程会获得一个锁，全权负责本次队列中的所有事务的落盘操作，接着联系其他follower线程，将他们的提交内容获取到，并让他们等待自己完成操作，接着这个leader线程会进入后续的落盘过程，等完成后，会通知本队列的所有follower线程落盘已经完成，可以返回成功状态了。
                好处：
                    多次磁盘IO合并成一次IO，减少读写次数，有待提高
        5.7版本： 多线程复制
            5.7是主从复制上一个划时代版本，在原来组提交优化了磁盘IO效率的基础上，将组提交的模式应用到了主从网络IO上，对网络效率进行了同样的优化，官方称之为：Enhanced Multi-Threaded Slave（简称MTS），增强的多线程备份。
## 5.MYSQL线程模型
    5.0）区分连接池和线程池
        5.0.1)连接池在客户端设置：作用是避免频繁创建和销毁连接
        5.0.2)线程池在DB服务器设置：超过最大设置，进入队列，控制MYSQL服务器活动线程数目；高并发时保护DB
            连接和线程模型不是1:1，而是M:N;
        5.0.3）MYSQL线程池架构：
            ThreadPool由一个Timer线程和多个Thread Group，每个Group又由两个队列、一个lintener线程和多个worker线程构成。
            - 队列：用来存放执行的IO任务，分高优先级和低优先级：事务语句放高
            - listener线程：监听该线程Group的语句，并确定当自己变成worker线程，是立即执行语句还是入队，判断标准是 队列是否有待执行的语句
            - worker线程：干活的线程
            - Timer线程：用来周期性检查Group是否阻塞，若阻塞，则唤醒或者new线程来解决，queue_event_count 和IO任务队列是否为空来判断。
        5.0.4）Thread Pool工作流程：
            1. 请求进入Mysql，根据threadID%/thread_pool_size确定Group；
            2. Group的listener线程监听有新的请求时，检查队列是否有请求还未处理 。若无，自己转换为worker立即处理该请求，如果队列还有未处理的请求，则将该请求入队，让其他线程处理；
            3. Group中的thread线程检查队列的请求，若队列有请求，则进行处理，否则休眠，查过thread_pool_idle_timeout自动退出。
            4. Timer线程检查各Group是否有阻塞，若有，就对worker线程唤醒或创建新worker线程。
        5.0.5）Thread Pool分配机制
            线程池根据thread_poo_size分成若干Group，每个Group各自维护客户端连接，客户端发起连接时，会根据threadid对thread_pool_size取模，落到对应Group
            thread_pool_oversubscribe参数控制每个group的最大并发线程数，每个group的最大并发线程数为thread_pool_oversubscribe+1个。若对应的group达到了最大的并发线程数，则对应的连接就需要等待。这个分配机制在某个group中有多个慢SQL的场景下会导致普通的SQL运行时间很长，


    5.1）MYSQL SERVER层线程模型
        5.1.1）采用poll机制为客户端每个链接创建一个线程；
        5.1.2）ThreadCache减少创建线程的开销，有waiting_thd_list队列/
        mysql_cond_wait/signal/mysql_mutex_unlock/条件变量：COND_thread_cache/
        并没有对线程并发进行控制；线程切换性能损耗严重；
        5.1.3）有linstner线程，worker线程，优先级队列，普通队列，dumper线程
    5.2）MYSQL 存储引擎层InnoDB线程模型
        5.2.1）可配置引擎层线程并发数量，线程进入内核需要申请tickets，两种申请方式：一是内核通过mutex和cond实现tickets申请；一是无锁方式（while-true循环）申请tickets。申请不到tickets的线程进入等待；

        5.2.2）Thread Pool，解绑连接 和线程，epoll实现
## 6.索引结构，B+树各个引擎实现有何不同？
## 7.引擎区别，为何默认是innodb或者myisam
    5.5之前是MyIASM，之后是innodb
    1、MyIASM是非事务安全的，而InnoDB是事务安全的
    2、MyIASM锁的粒度是表级的，而InnoDB支持行级锁
    3、MyIASM支持全文类型索引，而InnoDB不支持全文索引
    4、MyIASM相对简单，效率上要优于InnoDB，小型应用可以考虑使用MyIASM
    5、MyIASM表保存成文件形式，跨平台使用更加方便
## 8.MVCC机制
    数据库并发事务控制方法之一，
    7.1）并发事务控制有三种：
        悲观锁：最常用，锁两种模式：读锁共享锁/写锁排他锁
            读写锁、
        乐观锁: 
            基于时间戳的乐观并发协议：
                每个事务都拥有一个全局唯一的时间戳（系统时钟/计数器），保证唯一且递增；
            基于验证的协议：
                读->验证->写，三个时间戳
        多版本控制MVCC：
            每个写事务都创建一个版本，每个读事务会挑选合适的版本进行读
    7.2）Mysql与MVCC
        Mysql将MVCC与2PC取优结合，每个版本的数据行都有唯一的时间戳，读事务取最大时间戳的行；写事务：先读取最新版本数据，计算，创建新版本（时间戳：目前数据行版本+1），     
