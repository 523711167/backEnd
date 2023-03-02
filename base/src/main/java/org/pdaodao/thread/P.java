package org.pdaodao.thread;

/**
 * 1.synchronized关键字
 *      1。synchronized (对象)、实例方法上synchronized
 *      2。synchronized (class类)、静态方法synchronized
 *      无论是方法上还是静态方法上最终会转化成synchronized语法，
 *  多线程访问同步代码块，且竞争是同一把类锁或者对象锁，没有竞争到的线程才会阻塞。
 *
 * 2.wait notify notifyAll
 *      1.必须持有对象监视器才可以执行notify notifyAll wait,比如synchronized (对象)
 *      的对象可以使用
 *      wait ：当前线程one放弃A对象监视器（以下统称A）所有权进入等待状态，
 *      直到持用A所有权two线程调用notify、notifyAll唤醒one线程，重新竞争A所有权
 *      notify：唤醒任意一个等待A对象监视器的线程two，
 *      等待当前one线程放弃A监视器所有权（执行完毕）,two线程重新竞争监视器所有权(比如阻塞的线程)
 *      notify: 就绪所有等待A对象监视器的线程，~~~~~~~
 *
 * 3.yield join interrupt sleep
 *      1。都是Thead对象上的方法
 *      yield：使当前线程处于就绪状态,等待cpu分配时间片段
 *      join:  A.join()通过源码发现有wait方法，当前线程进入等待状态，
 *      在A线程执行完成后，会调用notifyall()
 *      interrupt: A线程设置中断标示位，不影响线程运行，开发人员通过isInterrupted,
 *      获取标示位，判断是否要停止程序，线程wait和sleep还有join期间，调用
 *      interrupt(),会抛出异常，同时清除中断标识位。lock和synchronized阻塞状态
 *      thread.interrupt()是无法打断的。
 *      sleep: 使当前线程处于休眠状态，但是不会放弃A监视器所有权
 *
 * 5.java内存模型
 *      JVM的内存模型分为主内存和每个线程的工作内存，工作内存持有
 *      主内存变量副本，工作内存变量修改在同步到主内容，但是主内容
 *      无法即使通知其余工作内容更新副本。
 *
 * 6.volatile关键字
 *      volatile修饰的变量A可以保证对所有工作内存的可见性。
 *      A如果发生修改，会导致工作内存的副本A失效，然后强制
 *      所有工作内存同步主内存变量A，并且整个过程是具备原子性的。
 *
 * 6.unsafe类
 *      1。可以在线程任意地方是使用和 wait notify notifyAll不一样
 *      unpark
 *      park
*          true 表示入参为绝对值时间
 *              long time = System.currentTimeMillis()+3000;
 *              表示3秒后自动唤醒，park(true,time + 3000) 毫秒
 *         false
 *              park(false,3000000000L) 纳秒
 *
 * 7.LockSupport类
 *        1.不需要在同步代码块中使用
 *        2.unpark函数可以先于park调用
 *        3.unpark函数可以唤醒指定的线程
 *        LockSupport.park();
 *        LockSupport.unpark();
 *
 * 7.锁的分类
 *        1。公平锁和非公平锁
 *            公平锁
 *            严格按照队列排列顺序中获取锁，没有在队列中的线程在
 *            竞争锁，会主动排队到队列末尾。
 *            非公平锁
 *            已经在队列中排队的线程按照顺序出列，但是还是会和没有在
 *            队列排队的线程竞争锁
 *        2。可重入锁和不可重入锁
 *            可重入锁 线程获取锁之后，访问共享资源期间，可以重复获同一把取锁
 *            不可重入锁  反之
 *        3。共享锁和独占锁（排他锁）
 *            共享锁 可被多个线程所持有
 *            独占锁 一次只能被一个线程获取
 *        4。乐观锁和悲观锁
 *            乐观锁 乐观锁通过某种方式不加锁来处理资源，使用版本号机制和CAS算法实现
 *            悲观锁 每次读取共享数据的时候都会上锁，禁止别的线程访问
 *        5。偏向锁 轻量级锁 重量级锁
 *            偏向锁 当线程A获取到锁，锁对象头会记录线程A的ThreadId,这个时候表示设置偏向锁成功
 *            轻量级锁 线程B访问锁，发现记录不是线程B的ThreadId,会检测记录的线程ThreadId是否存活，
 *                    如果不是存活状态，将锁对象头重新设置为线程B的ThreadId，如果存活升级为轻量级锁
 *                    通过CAS修改ThreadId,修改成功的线程获取到锁
 *            重量级锁 当多个线程通过cas修改threadId失败达到一定次数，升级为重量级锁
 * 8. Lock接口
 *      lock 尝试获取锁，获取成功则返回，否则阻塞当前线程
 *      lockInterruptibly() 尝试获取锁，线程在成功获取锁之前被中断，则放弃获取锁，抛出异常
 *      tryLock() 尝试获取锁，获取锁成功则返回true，否则返回false
 *      tryLock(long time, TimeUnit unit)  尝试获取锁，若在规定时间内获取到锁，则返回true，否则返回false，未获取锁之前被中断，则抛出异常
 *      unlock()  释放锁
 *      newCondition() 返回当前锁的条件变量，通过条件变量可以实现类似notify和wait的功能，一个锁可以有多个条件变量
 * 9. Condition接口
 *      让线程进入等待通知的状态
 *      await()
 *      awaitUninterruptibly();
 *      long awaitNanos(long nanosTimeout)
 *      boolean await(long time, TimeUnit unit)
 *      boolean awaitUntil(Date deadline)
 *
 *      signal()
 *      signalAll();
 * 10.AbstractQueueSynchronizer类
 *      state：代表同步状态 0代表没有同步 1或者大于1代表重入次数
 *      head： 代表AQS维护队列的头
 *      tail： 代表AQS维护队列的尾
 *      父类属性
 *      exclusiveOwnerThread：代表独有同步模式为那个线程所有
 *      （大白话：那个线程获取到了锁）
 *
 * 11. Node类 双向队列
 *          waitstate -1表示后继节点的阻塞状态
 *          next      下个节点
 *          prev      后继节点
 *          thread    当前节点保存的线程
 * 12. Sync类
 *         继承Sync继承AbstractQueuedSynchronizer
 *
 * 13. CountDownLatch类
 * 14. CyclicBarrier类
 * 15. 线程池
 * 16. ReentrantReadWriteLock
 *      读锁
 *          写锁没有被获取的时候，读锁可以被多个线程访问
 *          写锁被获取的时候，读锁可以被获取写锁的线程访问
 *      写锁
 *          写锁不可被多个线程访问
 *          读锁获取的时候，写锁不可以被访问
 *
 *
 */
public interface P {
}
