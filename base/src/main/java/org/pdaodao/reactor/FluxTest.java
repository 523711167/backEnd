package org.pdaodao.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

public class FluxTest {

    /**
     * fromIterable的使用
     */
    @Test
    public void test() {
        // 创建一个 List 集合
        List<String> list = Arrays.asList("A", "B", "C", "D", "E");
        // 使用 fromIterable 方法将 List 转换为 Flux 对象
        Flux<String> flux = Flux.fromIterable(list);
        // 针对每个元素都输出一次
        flux.subscribe(System.out::println);
    }

    /**
     * concatMap的使用
     * 有点扁平化的意思
     */
    @Test
    public void test1() {
        // 创建一个包含多个元素的 Flux 流
        Flux.just(1, 2, 3)
                // 将每个源元素 i 映射为一个包含多个元素的 Flux 对象，并使用 concatWith 方法连接
                .concatMap(i -> Flux.just("A" + i, "B" + i))
                // 输出结果
                .subscribe(System.out::println);
    }

}
