package org.pdaodao.lambda;

import java.util.Date;

class PinDaoDao implements Person {

    @Override
    public void work(Date date) {
        System.out.println(date);
    }
}
