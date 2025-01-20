package org.pdaodao.thread.waitAndNotifyAllAndInterrupt;


import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class P {



    /**
     * 虚假唤醒
     *    线程没有在显示调用notify()或者notifyAll()方法情况下，从wait()状态中被唤醒，这种情况发生于java虚拟机的实现和操作系统的调度。
     *
     * 这个案例并没有发生虚假唤醒，主要是借此说明虚假唤醒。
     * 假如在
     *     if (anInt != 0) {
     *             this.wait();
 *         } 代码块发生虚假唤醒，会继续执行if后的逻辑，但是注意this.wait()，不是由notifyAll唤醒，因此有问题。
     */
    @Test
    public void test232() throws InterruptedException {
        A a = new A();

        Thread aa = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    a.spuriousWakeupIncr();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "aa");


        Thread bb = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    a.spuriousWakeupDecr();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "bb");
        aa.start();
        bb.start();

        TimeUnit.SECONDS.sleep(50);
    }


    /**
     *  这个案例解决虚假唤醒，通过while判断条件是否成立，
     *  while中的逻辑判断来源：
     *      另一个线程进入共享代码块(共享同一把锁)，执行完代码逻辑后（逻辑判断来源），调用notifyAll方法。
     */
    @Test
    public void test() throws InterruptedException {
        A a = new A();

        Thread aa = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    a.incr();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "aa");

        Thread bb = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    a.decr();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "bb");

        aa.start();
        bb.start();

        TimeUnit.SECONDS.sleep(50);
    }

    /**
     * 测试synchronized执行完毕是否会调用notifyAll
     * 不会,bb死亡也没调用notifyAll，线程aa没有执行wait后的方法
     */
    @Test
    public void test23() throws InterruptedException {
        CC cc = new CC("测试");
        Thread aa = new Thread(() -> {
            try {
                cc.ceWork();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "aa");

        Thread bb = new Thread(() -> {
            try {
                cc.ceWorkWork();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "bb");


        aa.start();
        TimeUnit.MILLISECONDS.sleep(100);
        bb.start();

        System.out.println("t.isAlive() = " + bb.isAlive());
        TimeUnit.SECONDS.sleep(5);
        System.out.println("t.isAlive() = " + bb.isAlive());

        TimeUnit.SECONDS.sleep(10);
    }

    /**
     *  thread.interrupt(); 设置线程打断标志，但不会修改线程的运行状态.
     *  thread.interrupt()后 isInterrupted返回true
     */
    @Test
    public void test22() throws InterruptedException {
        AtomicInteger i = new AtomicInteger();
        Thread thread = new Thread(() -> {
            while (true) {
                System.out.print(i.incrementAndGet() + "===");
                System.out.println("Thread.currentThread().isInterrupted() = " + Thread.currentThread().isInterrupted());
            }
        });

        thread.start();
        // MILLISECONDS 毫秒 1000 = 1s
        // MICROSECONDS 微秒 1000 = 1 毫秒
        // NANOSECONDS  纳秒 1000 = 1 微秒
        TimeUnit.NANOSECONDS.sleep(1);
        System.out.println("thread.isInterrupted() before = " + thread.isInterrupted());
        thread.interrupt();
        System.out.println("thread.isInterrupted() after = " + thread.isInterrupted());
        TimeUnit.NANOSECONDS.sleep(5);
    }

    /**
     * 设置aa.interrupt()，测试aa线程在sleep状态，抛出异常同时会清除中断标识位。
     * 1。如果线程是在wait 和 sleep join 的状态，可以通过其余线程调用等待线程变量Thread.interrupt()，
     *    中断等待过程，抛出异常
     * 2。interrupt无法打断阻塞等待synchronized、lock锁的线程
     */
    @Test
    public void test1() throws InterruptedException {
        AtomicInteger i = new AtomicInteger();

        Thread aa = new Thread(() -> {
            while (true) {
                int i1 = i.incrementAndGet();
                System.out.println(i1 + "sleep before = " + Thread.currentThread().isInterrupted());
                try {
                    // sleep方法抛出异常
                    TimeUnit.NANOSECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(i1 +  "sleep after = " + Thread.currentThread().isInterrupted());
            }
        }, "aa");

        aa.start();
        TimeUnit.MICROSECONDS.sleep(10000);
        System.out.println("thread.interrupt() before = " + aa.isInterrupted());
        aa.interrupt();
        System.out.println("thread.interrupt() after = " + aa.isInterrupted());
        TimeUnit.MICROSECONDS.sleep(10000);
    }

    /**
     * 测试wait方法，在设置interrupt是否抛出异常
     * 会
     */
    @Test
    public void test4739483() throws InterruptedException {
        Thread aa = new Thread(() -> {
            test2222222();
        }, "aa");

        aa.start();
        TimeUnit.MICROSECONDS.sleep(10000);
        System.out.println("thread.interrupt() before = " + aa.isInterrupted());
        aa.interrupt();
        System.out.println("thread.interrupt() after = " + aa.isInterrupted());
        TimeUnit.MICROSECONDS.sleep(10000);
    }

    /**
     * 测试join，在设置interrupt是否抛出异常
     */
    @Test
    public void test7498493() throws InterruptedException {
        Thread aa = new Thread(() -> {
            while (true) {
                System.out.println("aa");
            }
        }, "aa");

        Thread cc = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                aa.interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "cc");

        aa.start();
        cc.start();
        try {
            // 在主线程执行，判断aa线程是否存活，aa线程的wait(0)
            // 目的：aa执行完毕后在执行bb
            aa.join(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TimeUnit.NANOSECONDS.sleep(1);
    }

    public synchronized void test2222222() {
        System.out.println("wait before = " + Thread.currentThread().isInterrupted());
//        try {
//            this.wait();
//        } catch (InterruptedException e) {
//           e.printStackTrace();
//        }
        System.out.println("wait after = " + Thread.currentThread().isInterrupted());
    }


    static Lock lock = new ReentrantLock();

    /**
     * 测试interrupt 对lock无效
     */
    @Test
    public void test2() {
        Thread aa = new Thread(() -> {
            try {
                lock.lock();
                System.out.println("t got lock");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        Thread bb = new Thread(() -> {
            lock.lock();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        });

        bb.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        aa.start();
        aa.interrupt();
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
     * join实际上就是调用wait方法，但是wait的是当前所在运行线程，不是调用join的线程。
     */
    @Test
    public void test3() throws InterruptedException {
        Thread a = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("A");
            }
        });

        Thread b = new Thread(() -> {

            for (int i = 0; i < 10; i++) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
     * 张三到30调用yield，让CPU的调度时间，但是不完全让。
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


