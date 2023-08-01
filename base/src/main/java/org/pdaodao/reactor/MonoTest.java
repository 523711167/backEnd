package org.pdaodao.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MonoTest {

    /**
     * mono.subscribe(System.out::println);才会执行
     * @throws InterruptedException
     */
    @Test
    public void test1() throws InterruptedException {
        Mono<String> mono = Mono.<String>just("hello Wrold");
        Thread.sleep(Duration.ofSeconds(6).toMillis());
        //mono.subscribe(System.out::println);
    }

    /**
     * 此时，如果我们订阅 mono 对象两次，第一次订阅将执行 Mono 并缓存结果，
     * 第二次订阅将直接从缓存中获取结果,由于缓存 Mono 的结果会占用一定的内存空间，因此要谨慎使用 cache() 方法。
     * 它适用于那些不经常变动且结果可重复利用的 Mono 序列，例如从数据库或网络获取的静态数据。
     */
    @Test
    public void test2() {
        Mono<String> mono = Mono.just("hello").cache();
        mono.subscribe(System.out::println); // 执行 Mono 并输出 "hello"
        mono.subscribe(System.out::println); // 直接从缓存中输出 "hello"
    }

    /**
     * cast用作类型转换
     * @throws InterruptedException
     */
    @Test
    public void test3() throws InterruptedException {
        Mono<String> mono = Mono.<String>just("hello");
        //Mono<String> mono1 = mono.cast(String.class);
        Mono<Integer> mono1 = mono.cast(Integer.class);

        Thread.sleep(Duration.ofSeconds(6).toMillis());
        //mono1.subscribe(System.out::println);
    }

    /**
     *  证明Mono是异步的
     */
    @Test
    public void test4() throws InterruptedException {
        Mono.delay(Duration.ofSeconds(5))
                .map(d -> "hello")
                .doOnNext(System.out::println)
                .subscribe();

        Thread.sleep(Duration.ofSeconds(6).toMillis());

        System.out.println("world");
    }

    /**
     * doOnNext使用
     * 每个元素发射之前触发一些副作用操作，如打印日志、记录指标等，但是不会对数据元素本身做出任何修改
     * delay使用
     * 延迟两秒构建Mono
     */
    @Test
    public void test5() throws InterruptedException {
        Mono.delay(Duration.ofSeconds(2))
                .map(d -> "hello")
                .doOnNext(str -> System.out.println("on next: " + str))
                .subscribe();

        Thread.sleep(Duration.ofSeconds(3).toMillis());
    }

    /**
     * as使用
     * mono 转换 Integer
     */
    @Test
    public void test6() throws InterruptedException {
        Mono<String> mono = Mono.<String>just("123");
        Integer integer = mono.<Integer>as(str -> 1);

        Thread.sleep(Duration.ofSeconds(3).toMillis());
    }

    /**
     * map的使用
     * 可以遍历mono的数据(String)转换(Integer)  返回的Mono可以改变泛型
     */
    @Test
    public void test7() {
        Mono<String> mono = Mono.just("123");
        Mono<Integer> resultMono = mono.map(s -> Integer.valueOf(s));
        resultMono.subscribe(intVal -> System.out.println("Converted value: " + intVal));
    }

    /**
     * flatMap的使用
     * 可以遍历mono的数据(String)转换Mono 返回Mono可以改变泛型
     */
    @Test
    public void test8() {
        Mono<String> mono = Mono.just("123");
        Mono<Integer> monoi = mono.<Integer>flatMap(str -> Mono.<Integer>just(Integer.valueOf(str)));
        monoi.subscribe(intVal -> System.out.println("Converted value: " + intVal));
    }

    /**
     * doOnError的使用
     * 单纯的Consumer这个异常
     * 不会截获错误，只是在错误发生时执行自定义的操作,
     *  .map(val -> val * 2)
     *  .subscribe(System.out::println) 代码不走
     */
    @Test
    public void test9() {
        Mono.just("10A")
                .map(Integer::parseInt)
                .doOnError(ex -> System.err.println("Error occurred: " + ex.getMessage()))
                .map(val -> val * 2)
                .subscribe(System.out::println, error -> System.out.println("error.getMessage():" + error.getMessage()));
    }

    /**
     * onErrorResume的使用
     * Consumer这个异常，还要求返回Mono
     * 用于继续执行后续操作，并将错误抛弃。
     */
    @Test
    public void test10() {
        Mono.just("10a")
                .map(Integer::parseInt)
                .onErrorResume(ex -> Mono.just(-1))
                .map(val -> val * 2)
                .subscribe(System.out::println);
    }

    /**
     * onErrorReturn的使用
     * Integer类型替代值   返回Mono
     * 在出现错误时返回一个默认值，用于替代错误的结果值。
     * 如果返回一个空的 Mono,则会忽略这个错误并正常结束序列。
     */
    @Test
    public void test11() {
        Mono.just("10a")
                .map(Integer::parseInt)
                .onErrorReturn(-1)
                .map(val -> val * 2)
                .subscribe(System.out::println);
    }

    /**
     * Mono.error的使用 todo不太理解
     */
    @Test
    public void test12() {
        Mono.error(new RuntimeException("Something went wrong!"))
                .subscribe(System.out::println, ex -> System.err.println("Error: " + ex.getMessage()));
    }

    /**
     * then的使用
     * then会放弃上面结果，用新的结果
     */
    @Test
    public void test13() throws InterruptedException {
        Mono.just(2)
                .delayElement(Duration.ofSeconds(2))
                .map(i -> i * 2)
                .then(Mono.just("Hello World!"))
                .subscribe(System.out::println);

        TimeUnit.SECONDS.sleep(5);
    }

    /**
     * fromRunnable的使用,Mono整体是同步的
     */
    @Test
    public void test14() throws InterruptedException {
        Runnable runnable = () -> {
            // 一些异步操作代码
            System.out.println("Task completed!");
        };
        Mono.fromRunnable(runnable)
                .subscribe(System.out::println,
                        //异常信息
                        ex -> System.err.println("Error: " + ex.getMessage()),
                        //完成回调函数
                        () -> System.out.println("Runnable completed!"));
    }

    /**
     * defer 它不会在调用时创建 Mono 或 Flux 对象，而是在订阅时才会创建。
     * 每当有新的订阅时，都会调用该函数以创建一个新的序列对象。
     */
    @Test
    public void test15() {
        Mono<Integer> mono = Mono.defer(() -> {
            int value = new Random().nextInt(100);
            return Mono.just(value);
        });

        mono.subscribe(System.out::println); // 订阅1
        mono.subscribe(System.out::println); // 订阅2

        Mono<Integer> mono1 = Mono.just(new Random().nextInt(100));
        mono1.subscribe(System.out::println);
        mono1.subscribe(System.out::println);
    }

    /**
     * fromCallable todo
     */

    /**
     * subscriberContext使用 已经要删除,使用这个contextWrite替代
     */
    @Test
    public void test16() {
        //Mono.just("123")
        //        .subscriberContext(Context.of("userId", "我是你的爸爸"))
        //        .<Integer>map(Integer::valueOf)
        //        .as(var -> subscriberContext());

        //Flux<String> wordsFlux = Flux.just("hello", "world", "!");
        //Flux<String> modifiedFlux = wordsFlux
        //        .contextWrite(Context.of("timestamp", System.currentTimeMillis()))
        //        .doOnEach()
        //        .map(word -> word.toUpperCase() + subscriberContext());
        //modifiedFlux.subscribe(System.out::println);
    }

    /**
     *  doOnNext使用
     *  doOnNext 用于在处理元素时执行一些副作用操作，比如记录日志、调试、打印输出等。
     *  如果需要对元素进行转换或过滤等操作，则应该使用其他操作符，如 map()、filter() 等
     *  next()方法没有见着 todo
     */
    @Test
    public void test17() {
        Mono.empty().doOnNext(item -> {

        });
    }

    /**
     * blockLast使用
     * 阻塞当前线程直到 Mono 或 Flux 流中的所有元素都被处理完成，并返回流中的最终结果.
     */
    @Test
    public void test18() {
        int sum = Flux
                .range(1, 10)
                .doOnNext(System.out::println)
                .blockLast();
        // 输出流的总和
        System.out.println("Sum of Flux is: " + sum);

        // 创建一个无限制流，并使用 take 操作截取前 5 个元素
        Flux.interval(Duration.ofMillis(500))
                .doOnNext(System.out::println)
                .take(5)
                //使用 blockLast() 方法获取最后一个元素，并阻塞当前线程等待流处理完成
                .blockLast();
    }

    /**
     * checkpoint使用
     * 方法记录了 Mono 的位置信息，并在异常堆栈信息中输出了该位置信息所对应的方法调用链（Assembly trace）
     * 需要设置日记的级别为debug
     */
    @Test
    public void test19() {
        System.setProperty("log4j2.level", "DEBUG");
        Mono.just(1)
                .map(i -> i / 0)
                .checkpoint("my checkpoint")
                .subscribe(x1 -> System.out.println(x1), x -> System.err.println(x));
    }
}
