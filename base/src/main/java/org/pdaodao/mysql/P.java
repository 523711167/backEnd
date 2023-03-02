package org.pdaodao.mysql;

/**
 *
 * 聚集索引: 从物理存储的角度，索引可以分为聚集索引和非聚集索引，聚集索引表示数据和索引存储在一起，一般定义为主键，如果没有定义主键，innodb会取
 *          不为空唯一索引为聚集索引，如果都不满足条件，则会生成隐藏行作为聚集索引
 * 非聚集索引(辅助索引)：非聚集素索引表示索引和数据并不存放在一起。
 * hash索引：
 *      通过对Key进行hash计算得到hash值作为数据下标和value对应
 *      缺点：
 *          1。key键hash计算后的大小和key没有关系，不适用与范围查找和排序
 *          2。组合索引是多列一起通过hash计算，最左匹配原则失效
 *          3。无法避免表扫描，不同key键存在相同的hash值，而且如果存在大量的key键同hash值，也会导致效率底下。
 * mysql回表
 *      非聚集索引查询到数据，一般为主键信息，再通过聚集索引查询到mysql数据
 * mysql覆盖索引
 *      select查询的列包含索引列，不需要回表查询
 * mysql最左匹配原则
 *      复合索引
 * mysql索引下推
 *      索引下推是指mysql搜索引擎提前过滤搜索条件，再进行回表操作，通过减少回表操作，提高查询效率。
 *      案例：
 *          use表 username和age建立联合索引 , 查询语句select * from user2 where username='1' and age=99;
 *          无索引下推
 *              1。存储引擎通过索引找到username='1'的数据，通过回表操作找到对应行数据，返回给server层
 *              2。server层拿到数据后判断是否age=99，存在就返回给客户端，不存在就丢弃数据。
 *              3。name、age如果是普通索引，由于在username索引中，username字段存储是有序的，而B+tree叶子节点是双向链表连接，可以挨个找个返回给server，重复上诉操作，若是唯一索引，查询结束。
 *          索引下推
 *              1。存储引擎通过索引找到username='1'的数据，会继续判断age是否复合条件，如果复合条件，通过回表查询数据，返回给server层
 *              2。server层再判断是否含有其他非索引的过滤条件，再返回给客户端。
 *
 * mysql的执行顺序
 *     select t.name, q.password where t.user left join q.human on t.name = q.name where t.name = 'Lisi' group by t.name having t.record > 90 order by record limit 5
 *     1。user表和human笛卡尔绩，生成虚拟表v1
 *     2。on条件筛选数据，根据left join、right join、outer join添加数据
 *     3。根据where条件执行过滤条件
 *     4。执行group by函数
 *     5。执行having条件
 *     6。处理select字段
 *     7。处理distinct字段
 *     8。处理order by字段
 *     9。处理limit字段
 * mysql中join用法
 *      https://blog.csdn.net/weixin_42168230/article/details/113909672
 *     1。left join  取值A集合和B集合的并集，再加上A集合的所有数据
 *     2。right join 取值A集合和B集合的并集，再加上B集合的所有数据
 *     3. inner join 取值A集合和B集合的并集
 *     4. outer join 取值A集合和B集合的交集
 *
 * mysql索引类型
 *     1。普通索引：可以在任何一列创建。
 *     2。主键索引：在物理表的主键上建立索引。
 *     3。组合索引：选用多个字段建立索引。
 *         组合索引存在的优点
 *             1. （a，b，c）等于同时建立了a、ab、abc三个索引
 *             2. select a，b，c from dual where a = 1 and b = 2，可以减少回表操作，在索引中获取c的值
 *             3. 索引列越多，通过索引筛选的数据就越少，查询、排序、分页更加高效
 *     4。全文索引：
 *     5。空间索引：
 *
 *

 * mysql索引失效
 *      1。使用or关键字，必须要对前后字段都建立索引
 *      2。使用like关键字，除非like 'abc%',注意这种情况下，如果是联合索引，第二个不会走索引
 *      3. 联合索引的情况下，没有按照索引建立的顺序匹配where条件
 *      4。索引列有运算符号，!=、 <> 、not in
 *      5. 索引列使用了函数运算
 *      6。隐式类型转换 a = 1 但是a列是非int类型
 * mysql主从复制
 *      工作原理
 *          LogDump线程：主节点和从节点建立连接之后，监听BinaryLog文件变化，发送日志给从节点。
 *          IO/Thread：在主节点和从节点建立连接之后，用来接收LogDump线程发送过来的记录并且写入RelayLog文件中
 *          SQL Thread: 读取relay log文件内容，执行SQL，维护数据一致性.
 *          relayLog:  从节点中保留主节点发送过来的日志文件。
 *              statement: 记录主节点数据变化的操作语句，执行函数的时候会出现主节点和从节点不一致问题
 *              row: 记录主节点每行数据发生的变化，占用空间大
 *              mixed: statement和row两种模式混合
 *          binaryLog: 主节点数据方法变化日志文件。
 *
 *          主服务器上启动LogDump线程监听binaryLog文件变化，发送给从服务器，从服务器通过IO/Thread线程监听到主服务器发送的
 *          数据，写入relayLog，SqlThread获取relayLog的内容，解析为可执行的sql语句。
 * mysql和innodb和myisam区别
 *      innodb支持事务 myisam不支持事务
 *      innodb索引和db数据保存在一个文件中， myisam索引和db数据是分开保存
 *      innodb主键索引保存的db数据，其他索引保存的是主键key，需要回表查询， myisam索引保存都是数据地址指针
 *      innodb支持表级锁、行级锁，默认行级锁，myisam仅支持表锁
 *      innodb select性能慢 myisam select性能高
 * mysql的myisam为什么查询效率比innodb高
 *      1.innodb缓存索引和真实数据 myisam仅仅缓存索引和地址指针
 *      2.innodb还需要维护MVCC（多版本并发控制）
 * mysql的sql优化
 *      1.从数据库字段层面
 *      2.看执行计划、调整sql语句
 *      2.从索引层面
 *      3.分库分表、表结构重新设计
 *
 * mysql文件目录
 *      /usr/bin	客户端和脚本（mysqladmin、mysqldump 等命令）
 *      /usr/sbin	mysqld 服务器
 *      /var/lib/mysql	日志文件、socket 文件和数据库
 *      /usr/share/info	信息格式的手册
 *      /usr/share/man	UNIX 帮助页
 *      /usr/include/mysql	头文件
 *      /usr/lib/mysql	库
 *      /usr/share/mysql	错误消息、字符集、安装文件和配置文件等
 *      /etc/rc.d/init.d/	启动脚本文件的 mysql 目录，可以用来启动和停止 MySQL 服务
 * mysql的事物隔离级别
 *      1。读未提交 脏读 不可重复读 幻读
 *      2。读已提交 不可重复读 幻读
 *      3。可重复读 幻读
 *      4。串行化  事务按时间排序执行
 *          脏读：事务A读取到事务B未提交的内容
 *          不可重复读：事务A在事务B提交前后读取两次数据不一样，可以理解为读取到事务B提交的内容
 *          幻读：事务A在更新之后，读取到更新前不存在的数据
 * mysql索引越多越好吗
 *          1。索引需要占用磁盘的物理空间
 *          2。b+树在为了护卫树平衡需要进行页合并和页分裂操作
 * mysql索引最好不要使用uuid
 *          1。b+树是一颗有序多路树，uuid插入没法按照叶子节点最后位插入，会频繁引起树的合并和分裂。
 * mysql哪些列适合添加索引
 *          1.where条件字段
 *          2。join的连表字段
 *          3。排序字段
 *          4。group by后字段
 *          5。字段值的离散度越大，建立索引查询效率越明显
 * mysql的当前读和快照读
 *      当前读：读取的总是最新的数据,比如 update、insert、delete、for update都是读取最新的数据
 *      快照读：读取的总是历史版本记录，比如平常使用的select都是undolog历史版本数据
 * mysql的ACID实现的原理
 *      ACID解释
 *          原子性：事务中的SQL要么全部成功，要么全部失败。
 *              通过undolog记录数据的所有修改内容，一旦发生回滚，可以找到原始数据进行还原。
 *          一致性：
 *          隔离型：不同事务在不同隔离级别下对数据的访问有限制的
 *              通过MVCC(多版本并发控制)实现
 *          持久性：事务一旦提交，对数据的修改就是永久的
 *              数据的增删改更新磁盘都是随机IO，比较费时，如果大量的修改为刷新到磁盘，系统宕机，会导致数据丢失，
 *              所以mysql引入redolog，将修改的记录顺序写入redolog中，顺序IO比随机IO效率要高，如果发生系统宕机，
 *              从redolog中恢复。
 *
 *      表隐藏字段
 *          字段解释
 *              DB_TRX_ID 最后一次修改事务ID
 *              DB_ROW_ID 隐藏主键
 *              DB_ROLL_PTR 回滚指针指向undolog
 *      readview
 *          创建时机
 *              在不同的隔离级别下是有区别的
 *                  读已提交
 *                      在事务开启之后，每次快照读都会生成新的readview。
 *                  可重复读
 *                      在事务开启之后，只有第一次快照读的时候才会生成readview。
 *          字段解释
 *              trx_list 当前活跃事务ID
 *              up_limit_id 当前活跃最小事务ID
 *              low_limit_id 事务系统分配的下一个事务ID
 *什么是全表扫描
 *      对表中数据进行挨个遍历，直到最后一条数据
 *什么情况下会全表扫描
 *      ！=、<>、not in、in、or、count(*)查询
 *
 */
public interface P {
}
