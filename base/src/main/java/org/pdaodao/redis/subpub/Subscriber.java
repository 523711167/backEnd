package org.pdaodao.redis.subpub;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Subscriber extends Thread {

    private final JedisPool jedisPool;
    private final AcceptMessage accept = new AcceptMessage();

    private final String channel = "mychannel";

    public Subscriber(JedisPool jedisPool) {
        super("SubThread");
        this.jedisPool = jedisPool;
    }

    @Override
    public void run() {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.subscribe(accept, channel);
        } finally {
            jedis.close();
        }
    }
}
