package org.pdaodao.thread.future.completeFuture;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

/**
 * 比futureTask更加高级的CompletableFuture
 */
public class P {

    /**
     * 不带返回值的   runAsync
     * 带返回值结果的 supplyAsync
     */
    @Test
    public void test() throws InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(future.isDone());
        TimeUnit.SECONDS.sleep(5);
        System.out.println(future.isDone());
    }

    /**
     * thenRun      沿用上个任务的线程池
     * thenRunAsync 没有配置就使用默认的线程池，否则使用默认的线程池
     */
    @Test
    public void test4() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("CompletableFuture.runAsync = " + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, executorService).thenRunAsync(() -> {
            System.out.println("thenRunAsync = " + Thread.currentThread().getName());
        });

        System.out.println(future.isDone());
        TimeUnit.SECONDS.sleep(1);
        System.out.println(future.isDone());
        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * thenRun
     * thenAccept 可以接收上个参数
     * thenApply  可以接收上个参数
     * thenCompose可以接收上个参数 todo 不太理解
     * 都表示上个任务完成之后，下面的任务开始启动,参数不同
     */
    @Test
    public void test1() throws InterruptedException {
        CompletableFuture.<String>supplyAsync(() -> {
            System.out.println("我是第一个任务，我开始了");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("我是第一个任务，我完成了");
            return "我完成了，并且开始返回结果了";
        }).<String>thenCompose(x -> {
            return CompletableFuture.<String>supplyAsync(() -> {
                System.out.println("第二个任务我开始了");
                System.out.println("我发现我可以使用上一个任务的返回值结果,结果为: " + x);
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("第二个任务我完成了");
                return "第二个任务我返回结果了";
            });
        }).thenAccept(x -> {

        }).<Date>thenApply(x -> {
            return new Date();
        }).thenRun(() -> {

        });
        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * thenCombine
     * t1和t2是并列进行的，t1和t2的结果进行合并运算
     * <p>
     * thenAcceptBoth
     * t1和t2是并列进行的，t1和t2的结果进行合并运算,但是没有返回值
     * <p>
     * runAfterBoth
     * t1和t2是并列进行的, 完成之后在执行任务
     */
    @Test
    public void test3() throws InterruptedException {
        CompletableFuture<User> future = CompletableFuture.<Integer>supplyAsync(() -> {
            System.out.println("我是第一个任务，我开始了");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("我是第一个任务，我完成了");
            return 123;
        }).<String, User>thenCombine(
                CompletableFuture.supplyAsync(() -> {
                    System.out.println("我是第二个任务，我开始了");
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("我是第二个任务，我完成了");
                    return "我是第二个参数的结果";
                }), (num, str) -> {
                    Lisi user = new Lisi();
                    user.setName(str);
                    user.setAge(num);
                    return user;
                }
        );

        TimeUnit.SECONDS.sleep(10);
        System.out.println(future.join());
    }

    /**
     * allOf
     */
    @Test
    public void test22() {
        long start = System.currentTimeMillis();
        CompletableFuture<String> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("i am sleep 1");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "service 1";
        });
        CompletableFuture<String> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("i am sleep 2");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "service 2";
        });

        CompletableFuture<String> completableFuture3 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println("i am sleep 10");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "service 3";
        });
        CompletableFuture<Void> completableFuture = CompletableFuture
                .allOf(completableFuture1, completableFuture2, completableFuture3);
        System.out.println(completableFuture.join());
        System.out.println(System.currentTimeMillis() - start);

    }

    /**
     * applyToEither
     * acceptEither
     * runAfterEither
     * anyOf  任何一个CompletableFuture完成，anyOf函数就会返回。
     */
    @Test
    public void test33() throws InterruptedException {
        CompletableFuture.<Integer>supplyAsync(() -> {
            System.out.println("我是第一个任务，我开始了");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("我是第一个任务，我完成了");
            return 123;
        }).<User>applyToEither(
                CompletableFuture.<Integer>supplyAsync(() -> {
                    System.out.println("我是第二个任务，我开始了");
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("我是第二个任务，我完成了");
                    return 1234;
                }), x -> {
                    System.out.println("x = " + x);
                    User user = new User();
                    return user;
                });

        TimeUnit.SECONDS.sleep(10);
    }


    @Test
    public void test5() {
        BiFunction<Integer, String, User> biFunction = new BiFunction<Integer, String, User>() {

            @Override
            public User apply(Integer integer, String s) {
                return new Lisi();
            }
        };

        ArrayList<User> users = new ArrayList<>();
        users.add(new Lisi());
        users.add(new User());
    }
}

