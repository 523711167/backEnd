package org.pdaodao.thread.lock.rentrantlock;

import java.util.concurrent.atomic.AtomicReference;

class NoReenTrantTest {

    private AtomicReference<Thread> owner = new AtomicReference<Thread>(null);

    public void lock() {
        Thread thread = Thread.currentThread();
        for (; ; ) {
            if (owner.compareAndSet(null, thread)) {
                return;
            }
        }
    }

    public void unlock() {
        Thread thread = Thread.currentThread();
        owner.compareAndSet(thread, null);
    }
}
