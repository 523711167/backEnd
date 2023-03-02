package com.example.springbootdemo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

public class P {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public String distributeRedis() {
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, Duration.ofSeconds(10));

        if (!lock) {
            return "获取锁失败";
        }

        try {
            Integer number = (Integer) redisTemplate.opsForValue().get("stock");

            if (number > 0) {
                number = number - 1;
                redisTemplate.opsForValue().set("stock", number);
                return "抢购完成";
            } else {
                return "库存没有啦";
            }
        } finally {
            while (true) {
                redisTemplate.watch("lock");
                if (redisTemplate.opsForValue().get("lock").equals(uuid)) {
                    redisTemplate.setEnableTransactionSupport(true);
                    redisTemplate.multi();
                    redisTemplate.delete("lock");
                    List<Object> exec = redisTemplate.exec();
                    if (exec == null) {
                        continue;
                    }
                    redisTemplate.unwatch();
                    break;
                }
            }

        }
    }
}
