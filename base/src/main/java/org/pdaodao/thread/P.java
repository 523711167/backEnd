package org.pdaodao.thread;

/**
 * 1.synchronized关键字
 *      1。synchronized (对象)、实例方法上synchronized 同一个实例共享同一把锁
 *      2。synchronized (class类)、静态方法synchronized 同一个类共享同一把锁
 *      无论是方法上还是静态方法上最终会转化成synchronized语法，
 *  多线程访问同步代码块，且竞争是同一把类锁或者对象锁，没有竞争到的线程才会阻塞。
 *
 * 2.wait notify notifyAll
 *      使用条件：
 *          必须持有对象监视器才可以执行notify notifyAll wait,比如synchronized (对象) 的对象可以使用。
 *      wait ：
 *          解释：
 *              当前线程one放弃A对象监视器（以下统称A）所有权进入等待状态，直到持用A所有权的two线程调用notify、notifyAll唤醒one线程，重新竞争A所有权
 *      notify：
 *          发生条件：
 *              1. 可以在持有对象监视器期间主动调用方法。
 *              2. synchronized代码执行完毕自动调用。 并不会
 *          解释：
 *              唤醒任意一个等待A所有权的线程,但是当前线程并不会放弃A所有权，执行完毕后自动释放A所有权,唤醒线程争夺A所有权。
 *      notifyAll:
 *          解释：
 *              唤醒所有等待A所有权的线程。
 *
 * 3.yield join interrupt sleep
 *      解释：
 *          都是Thead对象上的方法。
 *      yield：
 *          解释：
 *              使当前线程处于就绪状态,等待cpu分配时间片段。
 *          使用场景：
 *              多个线程之间运行，让出CPU时间分配。
 *      join:
 *          解释：
 *              A.join()通过源码发现有wait方法,会判断A线程是否还存活,存活则当前线程进入等待状态，在A线程执行完成后，会调用notifyall(),
 *              当前线程继续执行。
 *          使用场景：
 *              1. A B线程执行有先后顺序。
 *              2. A线程等待B线程执行完毕，获取B线程结果，A线程继续执行。
 *      interrupt:
 *          解释：
 *              A线程设置中断标示位，但是不影响A线程运行，开发人员通过判断当前线程的isInterrupted,
 *              获取标示位，判断是否要停止程序。
 *          使用场景：
 *              线程B执行某个环节需要主动中断线程A的操作，设置标志位，线程A判断标志位，是否需要终止线程A。
 *          注意：
 *              1. 线程wait和sleep还有join期间，调用interrupt(),会抛出异常，同时清除中断标识位。
 *              2. lock和synchronized阻塞状态thread.interrupt()是无法打断的。
 *      sleep:
 *          解释：
 *              使当前线程处于休眠状态，但是不会放弃A监视器所有权。
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
 *      解释：
 *          可以在线程任意地方是使用和 wait notify notifyAll不一样。
 *      unpark
 *          解释：
 *              把线程从等待状态过渡为唤醒状态，类似wait
 *      park
 *          解释：
 *              这个方法可以使当前线程进入等待状态，等待时间截止，或者通过unpark唤醒。
*                true 表示入参为绝对值时间
 *                  long time = System.currentTimeMillis()+3000;
 *                  表示3秒后自动唤醒，park(true,time + 3000) 毫秒
 *               false
 *                   park(false,3000000000L) 纳秒
 *
 * 7.LockSupport类
 *      解释：
 *          所在java.util.concurrent.locks包下，内部实现就是unsafe
 *        1.不需要在同步代码块中使用 todo 不记得这个
 *
 * 7.锁的分类
 *        1。公平锁和非公平锁
 *            公平锁
 *              解释：
 *                  严格按照队列排列顺序中获取锁，没有在队列中的线程在竞争锁，会主动排队到队列末尾。
 *            非公平锁
 *              解释：
 *                  已经在队列中排队的线程按照顺序出列，但是还是会和没有在队列排队的线程竞争锁。
 *        2。可重入锁和不可重入锁
 *            可重入锁
 *              解释：
 *                  当前线程获取锁之后，访问共享资源期间，可以重复获同一把取锁
 *              不可重入锁
 *                  解释：
 *                      反之
 *        3。共享锁和独占锁（排他锁）
 *            共享锁
 *              解释：
 *                  可被多个线程所持有
 *              具体的：
 *                  ReentrantReadWriteLock
 *            独占锁
 *              解释：
 *                  一次只能被一个线程获取
 *              具体的:
 *                  ReentrantLock
 *        4。乐观锁和悲观锁
 *            乐观锁
 *              解释：
 *                  乐观锁通过某种方式不加锁来处理资源，使用版本号机制和CAS算法实现
 *              具体的：
 *                  1. 比较并交换原子操作实现。
 *                  2. 版本号控制实现。
 *            悲观锁
 *              解释：
 *                  每次读取共享数据的时候都会上锁，禁止别的线程访问
 *              具体的：
 *                  Synchronized
 *                  Lock
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
 *       解释：
 *          并发辅助类，用于解决多线程环境下的数据竞争问题。
 *       使用场景：
 *          多线程环境下，线程A需要等待其他线程完成后在继续执行。(Plus版Join,内部AQS实现)
 * 14. CyclicBarrier类
 *       解释：
 *          同步辅助类,用于实现线程间的等待/通知机制。
 *       使用场景：
 *          多线程环境下，线程A、B、C可以互相等待，所有线程执行到各自的屏障处等待，然后在统一执行。
 * 15. 线程池
 *      图形：
 *          核心线程数 -> 队列 -> 最大线程数 -> 拒绝策略
 *      执行计划：
 *          1. 创建线程执行任务，如果没有空闲线程且小于核心线程数，创建核心线程。
 *          2. 达到核心线程数，则任务放入队列。
 *          3. 队列已满,线程数量小于最大线程数，创建线程处理任务。
 *          4. 队列已满，线程达到最大线程数量，采用拒绝策略。
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
