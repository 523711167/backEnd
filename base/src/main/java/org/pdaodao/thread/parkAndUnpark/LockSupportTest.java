package org.pdaodao.thread.parkAndUnpark;


import org.junit.Test;

public class LockSupportTest extends A {

    public void work(){
        System.out.println("我是Lock");
    }

    public void test1() {
        super.work();
    }

    @Test
    public void test() {
        test1();
    }
}


