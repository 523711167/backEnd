package org.pdaodao.redis.subpub;

import org.junit.Test;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Scanner;


/**
 * redis 发布订阅机制
 */
public class P {

    public static void main(String[] args) {
        // 连接redis服务端
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "127.0.0.1", 6379);

        Subscriber subscriber = new Subscriber(jedisPool);
        Publisher publisher = new Publisher(jedisPool);

        publisher.start();
        subscriber.start();

    }

    @Test
    public void test() {
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println(input.nextLine());
        }
    }
}
