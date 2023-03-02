package org.pdaodao.thread.volatile2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class P {

    //volatile作用
    //前沿
    //  JVM的内存模型 有主内存 每个线程有自己的工作内存 每个线程更新无法及时更新其他线程工作内存的副本
    //解决办法volatile
    //  volatile修饰的变量可以保证工作内存的变量强制同步到主内存
    //  volatile修饰的变量写操作会导致其他线程工作内存的副本失效
    public static volatile int num = 0;


    public static AtomicInteger atomic = new AtomicInteger(0);

    @org.junit.Test
    public void test() throws InterruptedException {
        //一般是多少个线程就设置为多少
        final CountDownLatch countDownLatch = new CountDownLatch(30);
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    //自加操作
                    //只有写是具有原子性的,包括其他主内存通知其他工作线程更新值环节
                    //num++不具备原子性，取值 累加 赋值 三个操作
                    //num++;

                    //cas操作可以解决
                    //因为 比较 （取值） 累加 赋值 是具有原子性的
                    atomic.incrementAndGet();
                }
                //表示执行完毕，减1
                countDownLatch.countDown();
            }).start();
        }
        //等待计算线程执行完
        countDownLatch.await();
        System.out.println(atomic);
    }

}
