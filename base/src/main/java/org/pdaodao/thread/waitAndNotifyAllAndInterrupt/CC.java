package org.pdaodao.thread.waitAndNotifyAllAndInterrupt;

import javax.xml.crypto.Data;
import java.util.concurrent.TimeUnit;

class CC implements Runnable {

    private String name;

    public CC(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 50; i++) {
            System.out.println("" + name + "-----" + i);
            // 当i为30时，该线程就会把CPU时间让掉，让其他或者自己的线程执行(也就是谁先抢到谁执行)
            if (i == 30) {
                Thread.yield();
            }
        }
    }

    void work() throws InterruptedException {
        synchronized (this) {
            System.out.println("我进入了work,开始wait");
            this.wait();
            System.out.println("我被唤醒了");
        }
    }

    void workworkwork() throws InterruptedException {
        synchronized (this) {
            System.out.println("我进入了workworkwork,开始wait");
            Thread.currentThread().wait();
            System.out.println("我被唤醒了");
        }
    }

    void workwork() {
        synchronized (this) {
            System.out.println("我进入了workwork,开始wait");
            this.notify();
            System.out.println("我想试试可不可以唤醒work中的线程");
        }
    }


    void jump() {
        synchronized (Data.class) {
            System.out.println("我进入了jump");
            Data.class.notify();
            System.out.println("我想试试可不可以唤醒work中的线程");
        }
    }

    synchronized void ceWork() throws InterruptedException {
        System.out.println("我想测试synchronized执行完毕，是否会自动调用notifyAll");
        this.wait();
        System.out.println("我调用了wait方法，但是我没有通过notifyAll方法主动唤醒");
    }

    synchronized void ceWorkWork() throws InterruptedException {
        System.out.println("测试开始");
//        this.notify();
        TimeUnit.SECONDS.sleep(2);
//        System.out.println("如果this.notify注释了，那么务必把我也注释了，我使用来测试notify后的代码还会执行吗");
    }
}
