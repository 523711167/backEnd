package org.pdaodao.thread.future.futureTask;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * futureTask的使用
 */
public class P {

    public static void main(String[] args) {
        FutureTask<String> futureTask = new FutureTask<String>(() -> {
            TimeUnit.SECONDS.sleep(10);
            System.out.println("启动开始");
            return "返回结果";
        });

        Thread t1 = new Thread(futureTask);
        t1.start();
        System.out.println(futureTask.isDone());
    }
}
