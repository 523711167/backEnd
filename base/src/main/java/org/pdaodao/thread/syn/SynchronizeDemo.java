package org.pdaodao.thread.syn;


public  class  SynchronizeDemo {

    void work() throws InterruptedException {
        System.out.println("我开始工作");
        synchronized (this) {
            Thread.sleep(5000);
            System.out.println("开始疯狂的敲代码");
        }
        System.out.println("我结束工作");
    }

    void workwork() throws InterruptedException {
        System.out.println("我开始工作、工作");
        synchronized (SynchronizeDemo.class) {
            Thread.sleep(5000);
            System.out.println("开始疯狂的敲代码、敲代码");
        }
        System.out.println("我结束工作、结束工作");
    }

    static synchronized void cry() throws InterruptedException {
        System.out.println("我开始哭了");
        Thread.sleep(5000);
        System.out.println("开始疯狂的掉眼泪");
        System.out.println("我结束哭了");
    }

    void crycry() throws InterruptedException {
        System.out.println("我开始哭了、哭了");
        synchronized (SynchronizeDemo.class) {
            Thread.sleep(5000);
            System.out.println("开始疯狂的掉眼泪、掉眼泪");
        }
        System.out.println("我结束哭了、结束哭了");
    }
}

