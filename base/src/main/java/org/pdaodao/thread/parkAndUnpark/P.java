package org.pdaodao.thread.parkAndUnpark;

import sun.misc.Unsafe;

import java.lang.reflect.Field;


/**
 * unpark和park的使用
 */
public class P {


    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException, InterruptedException {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        // 将字段的访问权限设置为true
        field.setAccessible(true);
        // 因为theUnsafe字段在Unsafe类中是一个静态字段，所以通过Field.get()获取字段值时，可以传null获取
        Unsafe unsafe = (Unsafe) field.get(null);

        //线程1必须等待唤醒
        Thread thread1 = new Thread(() -> {
            System.out.println("线程1:执行任务");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            System.out.println("线程1:挂起，等待唤醒才能继续执行任务");
            unsafe.park(false, 0);
            System.out.println("线程1:执行完毕");
        });
        thread1.start();

        //线程2必须等待唤醒
        Thread thread2 = new Thread(() -> {
            System.out.println("线程2:执行任务");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            System.out.println("线程2:挂起，等待唤醒才能继续执行任务");
            unsafe.park(false, 0);
            System.out.println("线程2:执行完毕");
        });
        thread2.start();

        Thread.sleep(5000);
        System.out.println("唤醒线程2");
        unsafe.unpark(thread2);
        Thread.sleep(1000);
        System.out.println("唤醒线程1");
        unsafe.unpark(thread1);


        //线程3自动唤醒
        Thread thread3 = new Thread(() -> {
            System.out.println("线程3:执行任务");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            System.out.println("线程3:挂起，等待时间到自动唤醒");
            unsafe.park(false, 3000000000L);
            System.out.println("线程3:执行完毕");
        });
        thread3.start();

        //线程4自动唤醒
        Thread thread4 = new Thread(() -> {
            System.out.println("线程4:执行任务");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            System.out.println("线程4:挂起，等待时间到自动唤醒");
            long time = System.currentTimeMillis() + 3000;
            unsafe.park(true, time);
            System.out.println("线程4:执行完毕");
        });
        thread4.start();

    }
}
