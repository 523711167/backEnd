package org.pdaodao.redis.subpub;

import redis.clients.jedis.JedisPubSub;

public class AcceptMessage extends JedisPubSub {
    @Override
    public void onMessage(String channel, String message) {
        //订阅之后收到的消息
        System.out.println("订阅之后收到的消息");
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        //订阅频道
        System.out.println("订阅频道");
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        //取消订阅频道
        System.out.println("取消订阅频道");
    }
}
