package org.pdaodao.thread.waitAndNotifyAllAndInterrupt;

public class A {

    private int anInt = 0;

    public synchronized void incr() throws InterruptedException {

        while (anInt != 0) {
            this.wait();
        }
        System.out.println(Thread.currentThread().getName() + (Thread.currentThread().isDaemon() ? "是" : "不是") + "守护线程" + "当前的值为" + ++anInt);
        this.notifyAll();
    }

    public synchronized void decr() throws InterruptedException {
        while (anInt != 1) {
            this.wait();
        }

        System.out.println(Thread.currentThread().getName() + (Thread.currentThread().isDaemon() ? "是" : "不是") + "守护线程" + "当前的值为" + --anInt);
        this.notifyAll();
    }

    public synchronized void spuriousWakeupIncr() throws InterruptedException {

        if (anInt != 0) {
            this.wait();
        }
        System.out.println(Thread.currentThread().getName() + (Thread.currentThread().isDaemon() ? "是" : "不是") + "守护线程" + "当前的值为" + ++anInt);
        this.notifyAll();
    }

    public synchronized void spuriousWakeupDecr() throws InterruptedException {
        if (anInt != 1) {
            this.wait();
        }

        System.out.println(Thread.currentThread().getName() + (Thread.currentThread().isDaemon() ? "是" : "不是") + "守护线程" + "当前的值为" + --anInt);
        this.notifyAll();
    }
}
