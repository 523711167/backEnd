package org.pdaodao.thread.create;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

class C implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println(3);
        TimeUnit.SECONDS.sleep(5);
        System.out.println(33);
        return "3";
    }
}
