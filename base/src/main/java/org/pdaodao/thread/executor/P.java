package org.pdaodao.thread.executor;

import java.util.concurrent.*;

public class P {

    /**
     * AbortPolicy：中断抛出异常
     * DiscardPolicy：默默丢弃任务，不进行任何通知
     * DiscardOldestPolicy：丢弃掉在队列中存在时间最久的任务
     * CallerRunsPolicy：让执行当前任务的线程(调用excute的方法的线程)去执行任务(对比前三种比较友好一丢丢)
     *
     * SynchronousQueue(同步移交队列)：队列不作为任务的缓冲方式，可以简单理解为队列长度为零
     * LinkedBlockingQueue(无界队列)：队列长度不受限制，当请求越来越多时(任务处理速度跟不上任务提交速度造成请求堆积)可能导致内存占用过多或OOM
     * ArrayBlockintQueue(有界队列)：队列长度受限，当队列满了就需要创建多余的线程来执行任务
     */

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                5,
                2L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 300; i++) {
            int finalI = i;
            executor.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("当前线程" + Thread.currentThread().getName() + "正在执行任务" + finalI);
            });
        }
    }
}
