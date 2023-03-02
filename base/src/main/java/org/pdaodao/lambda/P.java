package org.pdaodao.lambda;

import java.util.Date;

/**
 * lambda表达式的演变过程
 */
public class P {

    public static void main(String[] args) {
        //最原始
        PinDaoDao classA = new PinDaoDao();
        classA.work(new Date());

        //进化版
        Person anonymousClassA = new Person() {
            @Override
            public void work(Date date) {
                System.out.println(date);
            }
        };
        anonymousClassA.work(new Date());

        //最终版
        Person lambdaClassA = date -> System.out.println(date);
        //A lambdaClassA = System.out::println;
        lambdaClassA.work(new Date());
    }

}

