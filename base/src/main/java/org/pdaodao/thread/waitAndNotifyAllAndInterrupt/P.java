package org.pdaodao.thread.waitAndNotifyAllAndInterrupt;


import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class P {

    /**
     * 虚假唤醒
     * 发生在if (条件) {this.wait()}中(此线程为线程1)，其他线程调用notifyall后，线程1抢到cpu片段，但是实际不满足if条件，执行业务代码
     * 需要通过while (true) 循环去判断
     */
    @Test
    public void test() {
        A a = new A();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    a.incr();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "aa").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    a.decr();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "bb").start();
    }

    /**
     * 测试synchronized执行完毕是否会调用notifyAll
     * 不会
     */
    @Test
    public void test23() throws InterruptedException {
        CC cc = new CC("测试");
        new Thread(() -> {
            try {
                cc.ceWork();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        Thread t = new Thread(() -> {
            try {
                cc.ceWorkWork();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
        TimeUnit.SECONDS.sleep(5);
        System.out.println("t.isAlive() = " + t.isAlive());
        TimeUnit.SECONDS.sleep(50);
    }

    /**
     *  thread.interrupt(); 设置线程打断标志，但不会修改线程的运行状态.
     */
    @Test
    public void test22() throws InterruptedException {
        AtomicInteger i = new AtomicInteger();
        Thread thread = new Thread(() -> {
            while (true) {
                System.out.println(i.incrementAndGet());
            }
        });

        thread.start();
        TimeUnit.SECONDS.sleep(2);
        thread.interrupt();
        System.out.println("thread.isInterrupted() = " + thread.isInterrupted());
        TimeUnit.SECONDS.sleep(50);
    }

    /**
     * 测试sleep状态下，thread.interrupt()线程会抛出异常，同时会清除中断标识位
     * 1。如果线程是在wait 和 sleep join 的状态，可以通过其余线程调用等待线程变量Thread.interrupt()，
     *    中断等待过程，抛出异常
     * 2。interrupt无法打断阻塞等待synchronized、lock锁的线程
     */
    @Test
    public void test1() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println(Thread.currentThread().isInterrupted());
            }
        });

        thread.start();
        TimeUnit.SECONDS.sleep(2);
        thread.interrupt();
        TimeUnit.SECONDS.sleep(50);
    }

    static Lock lock = new ReentrantLock();

    @Test
    public void test2() {
        Thread t = new Thread(() -> {
            System.out.println("t = " + Thread.currentThread().isDaemon());
            try {
                lock.lock();
                System.out.println("t got lock");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        new Thread(() -> {
            System.out.println("new Thread = " + Thread.currentThread().isDaemon());
            lock.lock();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.start();
        t.interrupt();
        System.out.println("main = " + Thread.currentThread().isDaemon());
        System.out.println("结束了");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * @Test调试的时候要注意 子线程是守护的，但是我打印出来不是的
     *
     * join
     * join实际上就是调用wait方法，但是wait的是当前所在运行线程，不是调用join的线程，在调用join线程执行完毕之后，会自动执行notifyall
     */
    @Test
    public void test3() throws InterruptedException {
        Thread a = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 100; i++) {
                System.out.println("A");
            }
        });

        Thread b = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 100; i++) {
                System.out.println("B");
            }
        });

        Thread c = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 10; i++) {
                System.out.println("C");
            }
        });

        a.start();
        b.start();
        a.join();
        c.start();
        TimeUnit.SECONDS.sleep(10);
    }


    /**
     * 测试yield
     */
    @Test
    public void test5() throws InterruptedException {

        CC yt1 = new CC("张三");
        DD yt2 = new DD("李四");

        Thread thread = new Thread(yt1);
        Thread thread1 = new Thread(yt2);

        thread.start();
        thread1.start();

        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * 测试notify唤醒
     */
    @Test
    public void test6() throws InterruptedException {
        CC cc = new CC("张三");
        Thread thread = new Thread(() -> {
            try {
                cc.work();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread1 = new Thread(cc::workwork);

        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread1.start();
        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * 测试其余对象调用wait方法唤醒 不可以
     */
    @Test
    public void test9() throws InterruptedException {
        CC cc = new CC("张三");
        Thread thread = new Thread(() -> {
            try {
                cc.workworkwork();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread1 = new Thread(cc::workwork);

        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread1.start();
        TimeUnit.SECONDS.sleep(10);
    }


    /**
     * 测试持有不同监视器线程可以通过notify唤醒吗 不可以
     */
    @Test
    public void test7() throws InterruptedException {
        CC cc = new CC("张三");
        Thread thread = new Thread(() -> {
            try {
                cc.work();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread1 = new Thread(cc::jump);

        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread1.start();
        TimeUnit.SECONDS.sleep(10);
    }

}


