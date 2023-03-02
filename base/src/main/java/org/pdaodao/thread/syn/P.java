package org.pdaodao.thread.syn;

import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;


/**
 * synchronied锁的升级
 *      无锁状态->偏向锁->轻量级锁->重量级锁
 *      1.线程A第一次进入同步代码块,将当前线程ID写入锁对象的markwork中，当线程A再次获取进入
 *      同步代码块，发现线程ID一致，无需要任何操作，直接获取锁.当线程B访问同步代码块，发现线程ID不一致，
 *      升级为轻量锁，拷贝markwork到当前线程栈针，作为锁记录，并且通过cas修改markwork，修改成功，升级到轻量锁，
 *      如果失败，会通过cas自旋修改，当失败到一定的次数的时候，认为出现了竞争关系，升级为重量锁。
 *
 */
public class P {

    /**
     * 测试 synchronized (this)
     * 1.
     */
    @Test
    public void test() throws InterruptedException {
        SynchronizeDemo syn = new SynchronizeDemo();
        new Thread(() -> {
            try {
                syn.work();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                syn.work();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }).start();
        syn.work();

        TimeUnit.SECONDS.sleep(10);
    }


    /**
     * synchronized (this)和synchronized (SynchronizeDemo.class)
     * 在同一个实例下，多线程访问会出线竞争吗 不会
     */
    @Test
    public void test3() throws InterruptedException {
        SynchronizeDemo syn = new SynchronizeDemo();
        new Thread(() -> {
            try {
                syn.work();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                syn.workwork();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }).start();

        new Thread(() -> {
            try {
                syn.work();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * static synchronized 和(SynchronizeDemo.class)
     * 在不同实例下，会出现锁竞争吗 都会竞争一把锁
     */
    @Test
    public void test1() throws InterruptedException {
        SynchronizeDemo syn = new SynchronizeDemo();
        SynchronizeDemo syn1 = new SynchronizeDemo();
        SynchronizeDemo syn2 = new SynchronizeDemo();
        SynchronizeDemo syn3 = new SynchronizeDemo();

        new Thread(() -> {
            try {
                syn.cry();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                syn1.cry();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }).start();

        new Thread(() -> {
            try {
                syn2.crycry();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }).start();

        new Thread(() -> {
            try {
                syn3.crycry();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }).start();

        TimeUnit.SECONDS.sleep(100);
    }


    @Test
    public void test2() throws InterruptedException {
        Object o = new Object();
        System.out.println("还没有进入到同步块");
        System.out.println("markword:" + ClassLayout.parseInstance(o).toPrintable());
        //默认JVM启动会有一个预热阶段，所以默认不会开启偏向锁
        Thread.sleep(5000);
        Object b = new Object();
        System.out.println("还没有进入到同步块");
        System.out.println("markword:" + ClassLayout.parseInstance(b).toPrintable());
        synchronized (o){
            System.out.println("进入到了同步块");
            System.out.println("markword:" + ClassLayout.parseInstance(o).toPrintable());
        }
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void test5() throws InterruptedException {
        final Object obj = new Object();
        System.out.println("启动后对象布局：\n" + ClassLayout.parseInstance(obj).toPrintable());
        //JKD8延迟4S开启偏向锁
        Thread.sleep(5000);
        //可偏向 101
        final Object monitor = new Object();
        System.out.println("延迟5秒后对象布局：\n" + ClassLayout.parseInstance(monitor).toPrintable());
        //偏向锁
        synchronized (monitor) {
            System.out.println("对象加锁后的布局：\n" + ClassLayout.parseInstance(monitor).toPrintable());
        }
        System.out.println("对象释放锁后的布局：\n" + ClassLayout.parseInstance(monitor).toPrintable());

        System.out.println(monitor.hashCode());

        System.out.println("执行hash后的对象布局:\n" + ClassLayout.parseInstance(monitor).toPrintable());
        new Thread(() -> {
            synchronized (monitor) {
                System.out.println("线程2对象加锁后的布局：\n" + ClassLayout.parseInstance(monitor).toPrintable());
            }
        }).start();

        Thread.sleep(1000);

    }

}
