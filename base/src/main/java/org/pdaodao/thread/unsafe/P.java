package org.pdaodao.thread.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

/**
 *  unsafe类
 */
public class P {

    /**
     *  putObject 可以根据偏移量进行赋值操作
     *  objectFieldOffset   获取非static的Filed的偏移量
     *  staticFieldOffset   获取static的Filed的偏移量
     */
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);

        long offsetName = unsafe.objectFieldOffset(A.class.getDeclaredField("name"));
        long offsetPassword = unsafe.staticFieldOffset(A.class.getDeclaredField("password"));
        A a = new A();
        unsafe.putObjectVolatile(a, offsetName, "123");
        //todo 这个地方静态变量赋值无效
        unsafe.putObjectVolatile(a, offsetPassword, "321");
        System.out.println(a.getName());
        System.out.println(a.getPassword());
    }

    /**
     * 测试park和unpark
     */
    @org.junit.Test
    public void test() throws IllegalAccessException, NoSuchFieldException, InterruptedException {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);

        Thread t1 = new Thread(() -> {
            System.out.println("线程t1开始了");
            unsafe.park(false, 9000000000L);
            System.out.println("线程t1结束了");
        });

        Thread t2 = new Thread(() -> {
            System.out.println("线程t2开始了");
            System.out.println("线程t2结束了");
        });

        Thread t3 = new Thread(() -> {
            unsafe.unpark(t1);
            System.out.println("线程t3开始了");
            System.out.println("线程t3结束了");
        });

        t3.start();
        t3.join();
        t1.start();
        t2.start();


        TimeUnit.SECONDS.sleep(100);
    }
}


