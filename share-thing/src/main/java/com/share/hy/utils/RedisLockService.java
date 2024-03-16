package com.share.hy.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Redis 实现分布式锁
 */
@Component
@Slf4j
public class RedisLockService {

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 过期时间
     */
    private static final int EXPIRATION_TIME = 60000;

    /**
     * 加锁关键数据
     *
     * @param id
     * @return true:得到锁，false:未得到锁
     */
    public boolean tryLock(String id) {
        return tryLock(id, EXPIRATION_TIME);
    }

    /**
     * 加锁关键数据
     *
     * @param id
     * @return true:得到锁，false:未得到锁
     */
    public boolean tryLock(String id, int expirationTime) {
        String oldTime = stringRedisTemplate.opsForValue().getAndSet(id, System.currentTimeMillis() + "");
        if (StringUtils.isBlank(oldTime)) {
            return true;
        }
        if (System.currentTimeMillis() - Long.parseLong(oldTime) > expirationTime) {
            return true;
        }
        log.info("data is lock, id: {}", id);
        return false;
    }

    /**
     * 解锁关键数据
     *
     * @param id
     */
    public void unlock(String id) {

        stringRedisTemplate.delete(id);
    }

    /**
     * 延期解锁
     * @param seconds 秒
     * @param id 锁ID
     */
    public void setLockExpire(String id, int seconds) {
        stringRedisTemplate.expire(id, seconds, TimeUnit.SECONDS);
    }

    /**
     * Redis计数器
     * @param key 缓存KEY
     * @param seconds 时间周期
     * @param limit 限制数量
     * @return true: 大于限制数量, false: 小于等于限制数量
     */
    public boolean counterLimit(String key, int seconds, int limit) {
        Long current = stringRedisTemplate.opsForValue().increment(key);
        if (current == null) {
            log.warn("redis getAndSet failed, key: {}", key);
            return true;
        }
        if (current <= 1) {
            // 初次设置过期时间
            stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
            return false;
        } else {
            boolean result = current > limit;
            if (result) {
                log.info("counter limit, key: {}, second: {}, limit: {}, current count: {}", key, seconds, limit, current);
            }
            return result;
        }
    }
}
