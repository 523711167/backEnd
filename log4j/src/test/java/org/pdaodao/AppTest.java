package org.pdaodao;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.pxx.logdemo.LogDemoApplication;
import org.pxx.logdemo.controller.LogController;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = LogDemoApplication.class)
public class AppTest
{
    private final Log logger = LogFactory.getLog(LogController.class);


    @Test
    public void test() {
        logger.trace("trace");
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
    }
}
