package org.pdaodao.thread.lock.rentrantlock;

import java.util.concurrent.atomic.AtomicReference;

public class ReentrantTest {

    public static void main(String[] args) {
        //ReentrantTest reentrantTest = new ReentrantTest();
        //reentrantTest.lock();
        //reentrantTest.lock();

        ReentrantTest reentrantTest = new ReentrantTest();
        reentrantTest.lock();
        reentrantTest.lock();

        System.out.println("结束了");
    }


    private AtomicReference<Thread> owner = new AtomicReference<Thread>(null);
    private int state = 0;

    public void lock() {
        Thread thread = Thread.currentThread();

        if (owner.get() == thread) {
            state++;
            return;
        }

        for ( ; ; ) {
            if ( owner.compareAndSet(null, thread)) {
                return;
            }
        }
    }

    public void unlock() {
        Thread thread = Thread.currentThread();

        if (thread == owner.get()) {
            if (state != 0) {
                state--;
            } else {
                owner.compareAndSet(thread, null);
            }
        }
    }

}

