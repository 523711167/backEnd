package org.pdaodao.thread.planthread;

import java.util.concurrent.CountDownLatch;

public class CountDownLacthDome {

    public static void main(String[] args) throws InterruptedException {
        int numThreads = 5;
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            Thread thread = new WorkerThread(latch);
            thread.start();
        }

        // 等待所有线程完成操作
        latch.await();

        System.out.println("所有线程已完成操作");
    }
}

class WorkerThread extends Thread {
    private final CountDownLatch latch;

    public WorkerThread(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        // 模拟执行某个操作
        try {
            Thread.sleep(1000);
            System.out.println("线程完成操作");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 操作完成后调用 countDown() 方法
        latch.countDown();
    }
}