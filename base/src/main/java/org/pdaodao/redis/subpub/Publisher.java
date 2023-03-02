package org.pdaodao.redis.subpub;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Scanner;

public class Publisher extends Thread{


    private final JedisPool jedisPool;

    public Publisher(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public void run() {
        Scanner input = new Scanner(System.in);
        Jedis jedis = jedisPool.getResource();
        while (true) {
            String line = null;
            line = input.nextLine();
            if (!"quit".equals(line)) {
                jedis.publish("mychannel", line);   //从 mychannel 的频道上推送消息
            } else {
                break;
            }
        }
    }
}
