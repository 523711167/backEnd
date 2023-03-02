package org.pdaodao.stream;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * 
 */
public class P {

    private List<Integer> aa = Arrays.asList(2, 3, 23, 21, 1);

    private List <String> myList = Arrays.asList("dog", "over", "good");


    @Test
    public void test() throws InterruptedException {

        //单线程
        //与reduce不同的是，user不会出现累加的情况
        User pdd = aa.stream().<User>collect(() -> new User("pdd", null), (user, integer) -> {
            user.setAge(integer);
            System.out.println(user);
        }, (user, user2) -> {
            System.out.println("user = " + user);
        });

        //多线程
        //每个线程单独new User()得到的所有结果，在进行单线程运算
        User pdd1 = aa.parallelStream().<User>collect(() -> new User("pxx", null), (user, integer) -> {
            user.setAge(integer);
            System.out.println(user);
        }, (user, user2) -> {
            System.out.println("user = " + user);
        });

    }

    @Test
    public void test1() {
        //单线程情况
        //identity等于s1（初始值），s1和dog运算的结果为下次运算的s1，以此类推
        String reduce = myList.stream().reduce("", (s1, s2) -> {
            System.out.println(2);
            return s1 + s2.charAt(0);
        }, (c1, c2) -> {
            System.out.println(2);
            return c1 += c2;
        });
        System.out.println(reduce);

        //多线程
        //identity等于s1（初始值），s1和所有的s2同步运算，集合所有的结果，进行c1和c2的单线程运算
        String reduce1 = myList.parallelStream().reduce("", (s1, s2) -> {
            System.out.println(2);
            return s1 + s2.charAt(0);
        }, (c1, c2) -> {
            System.out.println(2);
            return c1 += c2;
        });
        System.out.println(reduce1);

    }

    @Test
    public void test3() {
        //在这种处理中，毫无意义
        //flatMap使用的业务场景是 在对象的多个属性或者是某个属性中，筛选出多个属性
        //比如 Stream<File> 需要把file转化为Stream<String>
        //map使用的业务场景是 在对象的多个属性中中筛检出某个属性
        aa.stream().<User>flatMap(x -> {
            User user = new User();
            user.setAge(x);
            return Stream.of(user);
        });

        aa.stream().map(x -> {
            User user = new User();
            user.setAge(x);
            return user;
        });
    }

}



