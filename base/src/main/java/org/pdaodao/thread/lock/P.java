package org.pdaodao.thread.lock;



import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

/**
 * Lock接口
 *      尝试获取锁，获取成功则返回，否则阻塞当前线程
 *  void lock();
 *      尝试获取锁，线程在成功获取锁之前被中断，则放弃获取锁，抛出异常
 *  void lockInterruptibly() throws InterruptedException;
 *      尝试获取锁，获取锁成功则返回true，否则返回false
 *  boolean tryLock();
 *      尝试获取锁，若在规定时间内获取到锁，则返回true，否则返回false，未获取锁之前被中断，则抛出异常
 *  boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
 *      释放锁
 *  void unlock();
 *      返回当前锁的条件变量，通过条件变量可以实现类似notify和wait的功能，一个锁可以有多个条件变量
 *  Condition newCondition();
 * <p>
 * 实现类
 * ReentrantLock  (ReentrantReadWriteLock) ReadLock WriteLock
 *
 * <p>
 * Condition接口
 *      让线程进入等通知待状态
 *  void await() throws InterruptedException;
 *  void awaitUninterruptibly();
 * <p>
 *      让线程进入等待通知状态，超时结束等待状态，并抛出异常
 *  long awaitNanos(long nanosTimeout) throws InterruptedException;
 *  boolean await(long time, TimeUnit unit) throws InterruptedException;
 *  boolean awaitUntil(Date deadline) throws InterruptedException;
 * <p>
 *      将条件队列中的一个线程，从等待通知状态转换为等待锁状态
 *  void signal();
 * <p>
 *      将条件队列中的所有线程，从等待通知阻塞状态转换为等待锁阻塞状态
 *  void signalAll();
 *
 *  CountDownLatch类
 *      主线程需要子线程执行完成才可以继续执行
 *      1.CountDownLatch countDownLatch = new CountDownLatch(2)
 *      2.countDownLatch.await()
 *      3.子线程执行完毕调用countDownLatch.countDown(),调用一次减一，减到0，主线程继续执行
 *
 *  CyclicBarrier类
 *      1.CyclicBarrier barrier = new CyclicBarrier(2)
 *      2.barrier.await() 2次之后程序继续执行，可以重复使用
 *
 * 前沿知识
 *  1.公平锁和非公平锁
 *      现在在执行await方法之后，会立即释放锁，进入等待队列,等待其余线程执行notify唤醒线程，
 *      执行notify之后，线程不会立即进入运行状态,而是处于就绪状态，然后获取锁，进入运行状态
 *      处于就绪状态同样会和没有进入同步队列中线程争抢<>锁</>
 *      没有进入同步队列的线程在没有竞争到锁之后，也会加入同步队列     非公平锁
 *      如果发现同步队列中不为空，同步队列线程为优先                公平锁
 *  2.可重入锁与非可重入锁
 *      线程获取锁之后，访问共享资源期间，可以重复获同一把取锁。    可重入锁
 *      反之                                              不可重入锁
 *  3.共享锁和独占锁(排他锁)
 *       一次只能被一个线程获取      独占锁
 *       可被多个线程所持有         共享锁
 *  4.乐观锁和悲观锁
 *       每次读取共享数据的时候都会上锁，禁止别的线程访问 悲观锁.
 *       乐观锁通过某种方式不加锁来处理资源，使用版本号机制和CAS算法实现、volalite关键字.
 */
public class P {

    volatile int key = 0;
    //可重入锁
    Lock l = new ReentrantLock();
    Condition c = l.newCondition();
    //读写锁
    //写锁没有被获取时候，多个线程可以同时获取读锁
    //写锁被获取，只有获取写锁的线程可以获取读锁，其余不可以获取
    //读锁被获取，写锁不可以被线程获取
    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();


    @Test
    public void test1() {
        Thread thread = new Thread(new A());
        Thread thread1 = new Thread(new B());


        thread1.start();
        thread.start();
        System.out.println("结束了");
    }

    /**
     * 测试LockSupport.park();是否会被thread.interrupt()打断
     * LockSupport.park(); 可以被thread.interrupt()打断，
     * 但是不会抛出异常，而且打断的状态isInterrupted会发生变化
     */
    @Test
    public void test4() throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("我开始获取锁了");
            l.lock();
            System.out.println("我开始等待");
            System.out.println("打断前 = " + Thread.currentThread().isInterrupted());
            LockSupport.park();
            System.out.println("打断后 = " +Thread.currentThread().isInterrupted());
            System.out.println("我可以被Interrupt打断？？？");
        });
        Thread thread1 = new Thread(() -> {
            thread.interrupt();
        });

        thread.start();
        TimeUnit.SECONDS.sleep(2);
        thread1.start();
        TimeUnit.DAYS.sleep(1);
    }

    public static void main(String[] args) {
        ReentrantLock l = new ReentrantLock();

        Thread thread = new Thread(() -> {
            l.lock();
            try {
                l.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            l.unlock();
        });
        thread.start();
    }

    /**
     *  测试ReentrantLock加锁过程
     *  AbstractQueueSynchronizer类简称AQS
     *  线程A执行第一次加锁
     *      通过CAS修改state，然后设置exclusiveOwnerThread为A
     *      表示当前lock对象为A所有
     *  线程B第二次加锁
     *      CAS修改失败，Lock已经被持有，如果是A执行再次加锁，state累加
     *      否则创建节点Node保存线程B，构建双向队列Queue，且给Lock对象设置头节点和尾节点,
     *      然后设置当前节点的后继节点(prev节点)waitStatus值为-1,然后调用LockSupport.park(this);
     *      B线程阻塞代码代码，通过CAS修改state，然后设置exclusiveOwnerThread为B
     *      替换AQS的头节点为当前节点，同时置空当前节点的thread和prev属性,然后执行同步代码
     *      知道线程B调用unlock
     *  线程C第三次加锁
     *      CAS修改失败，Lock已经被持有，如果是A执行再次加锁，state累加，
     *      否则创建节点Node报错线程C，追加到双向队列Queue尾部，替换Lock对象尾节点
     *      然后设置当前节点的后继节点(prev节点)waitStatus值为-1,然后调用LockSupport.park(this);
     */
    @Test
    public void test22() throws InterruptedException {
        ReentrantLock l = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            System.out.println("我是t1，我要开始加锁了");
            l.lock();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            l.unlock();
        });

        Thread t2 = new Thread(() -> {
            System.out.println("我是t2，我要开始加锁了");
            l.lock();
        });

        t1.start();
        TimeUnit.MILLISECONDS.sleep(100);
        t2.start();
        TimeUnit.MILLISECONDS.sleep(100);
        l.lock();
        System.out.println("我是上面解锁了才可以执行的");
        TimeUnit.DAYS.sleep(1);
    }

    /**
     *  测试ReentrantLock解锁过程
     *  AbstractQueueSynchronizer类简称AQS
     *  线程A第一次解锁
     *      判断是否为线程A解锁，ture更新state和exclusiveOwnerThread
     *      反之抛出异常, 获取头节点Node，修改头节点waitStatus值为0，唤醒
     *      Node.next()节点的线程。跳转到B线程阻塞代码（command+F查到）。
     *  线程B第二次解锁
     *  线程C第三次解锁
     */
    @Test
    public void test23() throws InterruptedException {
        ReentrantLock l = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            System.out.println("我是t1，我要开始加锁了");
            l.lock();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            l.unlock();
        });

        Thread t2 = new Thread(() -> {
            System.out.println("我是t2，我要开始加锁了");
            l.lock();
            l.unlock();
        });

        t1.start();
        TimeUnit.MILLISECONDS.sleep(100);
        t2.start();
        TimeUnit.MILLISECONDS.sleep(1000);
        l.lock();
        l.unlock();
        System.out.println("我是上面解锁了才可以执行的");
        TimeUnit.DAYS.sleep(1);
    }


    /**
     *  测试公平锁加锁过程
     *  AbstractQueueSynchronizer类简称AQS
     *  线程A第一次加锁
     *  线程B第二次加锁
     *  线程C第三次加锁
     */
    @Test
    public void test24() throws InterruptedException {
        ReentrantLock l = new ReentrantLock(true);
        Thread t1 = new Thread(() -> {
            System.out.println("我是t1，我要开始加锁了");
            l.lock();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            l.unlock();
        });

        Thread t2 = new Thread(() -> {
            System.out.println("我是t2，我要开始加锁了");
            l.lock();
            l.unlock();
        });

        t1.start();
        TimeUnit.MILLISECONDS.sleep(100);
        t2.start();
        TimeUnit.MILLISECONDS.sleep(1000);
        l.lock();
        l.unlock();
        System.out.println("我是上面解锁了才可以执行的");
        TimeUnit.DAYS.sleep(1);
    }

    class A implements Runnable {
        @Override
        public void run() {
            System.out.println("A = " + Thread.currentThread().isDaemon());
            int i = 10;
            while (i > 0) {
                l.lock();
                try {
                    if (key == 1) {
                        System.out.println("A is Running");
                        System.out.println("A is Running");
                        i--;
                        key = 0;
                        c.signal();
                    } else {
                        c.awaitUninterruptibly();
                    }

                } finally {
                    l.unlock();
                }
            }
        }

    }

    class B implements Runnable {
        @Override
        public void run() {
            System.out.println("B = " + Thread.currentThread().isDaemon());
            int i = 10;
            while (i > 0) {
                l.lock();
                try {
                    if (key == 0) {
                        System.out.println("B is Running");
                        i--;
                        key = 1;
                        c.signal();
                    } else {
                        c.awaitUninterruptibly();
                    }

                } finally {
                    l.unlock();
                }
            }
        }
    }
}




