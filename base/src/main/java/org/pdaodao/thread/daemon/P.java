package org.pdaodao.thread.daemon;

import org.junit.Test;

public class P {

    /**
     *  主线程结束，用户线程还未执行完毕则JVM不会停止
     *            守护线程还未执行完毕则JVM停止
     */
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100);
                    System.out.println("我是用户线程......");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();

        Thread.sleep(1000);
        System.out.println("主线程执行完毕......");
    }

    @Test
    public void test() {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(100);
                System.out.println("我是A用户线程......");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(100);
                System.out.println("我是B用户线程......");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        thread.start();
        thread1.start();
        System.out.println("主线程执行完毕......");
    }

}
