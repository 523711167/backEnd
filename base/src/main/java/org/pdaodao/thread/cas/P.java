package org.pdaodao.thread.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * valolite和cas实用案例
 */
public class P {

    //public static volatile Integer num = 0;
    public static AtomicInteger num = new AtomicInteger(0);

    public static void main(String[] args) {

        //输出的num有重复，因为不同线程间变量互相不可见
        //for (int i = 0; i < 5; i++) {
        //    new Thread(new Runnable() {
        //        @Override
        //        public void run() {
        //            while (num < 1000) {
        //                try {
        //                    Thread.sleep(10);
        //                } catch (InterruptedException e) {
        //
        //                }
        //                System.out.println("Thread name" + Thread.currentThread().getName() + "输出值" + num++);
        //            }
        //        }
        //    }).start();
        //}

        //通过volatile解决，还是会有多线程问题
        //for (int i = 0; i < 5; i++) {
        //    new Thread(() -> {
        //        while (num < 1000) {
        //            try {
        //                Thread.sleep(10);
        //            } catch (InterruptedException e) {
        //
        //            }
        //            System.out.println("Thread name" + Thread.currentThread().getName() + "输出值" + num++);
        //        }
        //    }).start();
        //}

        //cas解决并发的问题
        //todo 还是没有解决最后的并发问题
        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                while (num.get() < 1000) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {

                    }
                    System.out.println("Thread name" + Thread.currentThread().getName() + "输出值" + num.incrementAndGet());
                }
            }).start();
        }


    }
}
