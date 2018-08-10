
# Redis

## 1.Redis的两种持久化方式
    1.1)RDB/snapshotting（默认）
        默认存储到dump.rdb文件，可手动执行save或者bgsave命令，
        创建快照的方式：
        save命令：主线程运行，会阻塞请求；
        bgsave命令：Fork子进程运行
        配置文件策略：
            save 900 1//900秒之内，如果超过1个key被修改，则发起快照保存；
            save 300 10//300秒内，如果超过10个key被修改，则发起快照保存
        数据丢失：丢失最近一次快照之后的所有操作；
    1.2)AOF
        写命令追加存储到appendonly.aof
        配置文件：
            appendonly yes//开启
            appendfsync everysec//默认每秒持久化一次
            appendfsync always//每次操作都会写入aof文件，效率低
    区别：
    1）RDB 方式 记录整个数据集的所有信息，恢复快，Fork子进程对主进程不影响；
    2）AOF 记录命令操作，丢失少，多个命令Redis有优化策略；
## 2.Redis支持的数据结构五种对象类型
    Redis数据库键是字符串对象，值可以是五种对象类型，分别如下：
    2.1）REDIS_STRING    字符串对象(整形、浮点型、字符串)
        key/value存储
        命令：set/get/setex/setnx/incrby/incr/decrby/decr/exists/del/expire
        底层实现：或intset整数集合
    2.2）REDIS_LIST  列表对象
        双端链表结构
        命令：lpush/rpush/lrange/rpushx/lindex/lset/lrem/lpop/rpop/ltrim/linsert/rpoplpush
        底层实现：linkedlist和ziplist
    2.3）REDIS_HASH  哈希对象
        命令：hset/hget/hexist/hlen/hsetnx/hmset/hmget/hincrby/hincrby/hkeys/hvals/hgetall
        eg：hset(key,field,value)
        eg: hget(key,field)
        底层实现：散列
    2.4）REDIS_SET   集合对象
        命令：sadd/srem/smove/sismember/smembers
        底层实现：
    2.5）REDIS_ZSET  有序集合对象
        命令：zadd/
        底层实现：skiplist跳跃表和散列表
## 3.Redis8种底层数据结构
    参考：https://www.cnblogs.com/jaycekon/p/6277653.html
    参考：http://www.cnblogs.com/jaycekon/p/6227442.html
    REDIS_ENCODING_INT  long 类型的整数
    REDIS_ENCODING_EMBSTR   embstr 编码的简单动态字符串
    REDIS_ENCODING_RAW  简单动态字符串
    REDIS_ENCODING_HT   字典
    REDIS_ENCODING_LINKEDLIST   双端链表
    REDIS_ENCODING_ZIPLIST  压缩列表
        当列表、散列、有序集合的长度较短或者体积较小，Redis选择ziplist压缩列表紧凑方式来存储。
    REDIS_ENCODING_INTSET   整数集合
    REDIS_ENCODING_SKIPLIST 跳跃表和字典
## 4. Redis键过期时间
    4.1）针对列表、集合、有序结合、散列此类容器，只能针对整个键设置过期时间，而不能对其中元素设置过期时间；
    4.2）容器元素设置过期时间：通过对单个元素存储时间戳来实现

