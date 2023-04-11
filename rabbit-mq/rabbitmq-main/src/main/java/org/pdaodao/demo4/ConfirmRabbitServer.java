package org.pdaodao.demo4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.pdaodao.U;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ConfirmRabbitServer {

    private final static String QUEUE_NAME = "hello_confirm";

    /**
     * 发布确认
     * 场景：
     *  生产者发布消息到队列，确保消息百分之百持久化
     *  1. 开启队列持久化
     *  2. 开启消息持久化
     *  3. 开启发布确认
     *
     *  waitForConfirms方法等待broker返回确认
     */
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //noMultiple();

        multiple();
    }

    private static void multiple() throws IOException, TimeoutException, InterruptedException {
        Channel channel = U.createCon().createChannel();
        channel.queueDeclare(QUEUE_NAME, true, false, true, null);

        channel.confirmSelect();

        Integer total = 100;
        Integer burst = 10;

        StopWatch stopWatch = new StopWatch();
        for (int i = 0; i < total; i++) {
            String str = "我是第" + i + "次消息";
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, str.getBytes(StandardCharsets.UTF_8));
            if (i % burst == 0) {
                stopWatch.start("这个是第" + i + "次");

                System.out.println(channel.getNextPublishSeqNo());
                boolean b = channel.waitForConfirms();
                System.out.print("在" + i + "次消息的时候，我确认了一次");
                if (b) {
                    System.out.println("---而且我成功了");
                }
                stopWatch.stop();
            }
        }

        if (channel.waitForConfirms()) {
            System.out.println("以防万一，我还确认了一次");
        }

        System.out.println(stopWatch.prettyPrint());
    }

    private static void noMultiple() throws IOException, TimeoutException, InterruptedException {
        Channel channel = U.createCon().createChannel();
        channel.queueDeclare(QUEUE_NAME, true, false, true, null);

        channel.confirmSelect();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("消息开始发送");
        String str = "我是你爸爸";
        channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, str.getBytes(StandardCharsets.UTF_8));
        stopWatch.stop();

        boolean b = channel.waitForConfirms();
        System.out.println(channel.getNextPublishSeqNo());
        if (b) {
            System.out.println("接受成功");
        }

        System.out.println(stopWatch.prettyPrint());
    }
}
