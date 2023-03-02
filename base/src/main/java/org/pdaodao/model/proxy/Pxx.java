package org.pdaodao.model.proxy;

public class Pxx implements Person {
    @Override
    public String work(String name) {
        return "我的名字叫" + name;
    }
}
