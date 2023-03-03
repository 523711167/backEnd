package com.pxx.logdemo;

import com.pxx.logdemo.controller.LogController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class LogDemoApplicationTests {

    @Autowired
    LogController logController;

    @Test
    void contextLoads() {
    }

    //TRACE < DEBUG < INFO < WARN < ERROR < FATAL
    @Test
    public void test() {
        logController.log();
    }

}
