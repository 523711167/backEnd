package org.pdaodao.thread.waitAndNotifyAllAndInterrupt;

import javax.xml.crypto.Data;

class DD implements Runnable {

    private String name;

    public DD(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 50; i++) {
            System.out.println("" + name + "-----" + i);
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
}
