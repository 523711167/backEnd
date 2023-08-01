package org.pdaodao.thread.create;

import java.util.concurrent.FutureTask;

/**
 *  线程的创建方式3种
 */
public class P {
    public static void main(String[] args) throws Exception {

        //继承Thread类
        //Thread a = new A();
        //a.start();

        //实现Runnable
        //Thread b = new Thread(new B());
        //b.start();

        FutureTask target = new FutureTask(new C());
        Thread c = new Thread(target);
        c.start();
        //此处会阻塞等待返回值
        Object o = target.get();
        System.out.println(333);
    }

}

