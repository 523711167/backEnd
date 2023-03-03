package org.pdaodao.demo4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;
import org.pdaodao.U;

import java.io.IOException;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

public class AsyncConfirmRabbitServer {

    private final static String QUEUE_NAME = "hello_confirm";

    /**
     * 异步发布确认
     * 场景：
     */
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel = U.createCon().createChannel();
        channel.queueDeclare(QUEUE_NAME, true, false, true, null);

        channel.confirmSelect();

        /**
         *  线程安全有序的一个哈希表
         */
        ConcurrentSkipListMap<Long, String> csm = new
                ConcurrentSkipListMap<>();

        ConfirmCallback ackCallback = (sequenceNumber, multiple) -> {
            if (multiple) {
                //返回的是小于等于当前序列号的未确认消息 是一个 map
                ConcurrentNavigableMap<Long, String> confirmed =
                        csm.headMap(sequenceNumber, true);
                //清除该部分未确认消息
                confirmed.clear();
            }else{
                //只清除当前序列号的消息
                csm.remove(sequenceNumber);
            }
        };

        ConfirmCallback nackCallback = (sequenceNumber, multiple) -> {
            String message = csm.get(sequenceNumber);
            System.out.println("发布的消息"+message+"未被确认，序列号"+sequenceNumber);
        };

        //添加回调监听
        channel.addConfirmListener(ackCallback, nackCallback);

        Integer total = 100;

        for (int i = 0; i < total; i++) {
            String str = "我是第" + i + "次消息";
            csm.put(channel.getNextPublishSeqNo(), str);
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, str.getBytes());
        }
    }
}
