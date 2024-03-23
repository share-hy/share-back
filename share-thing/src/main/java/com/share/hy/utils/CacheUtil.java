package com.share.hy.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.share.hy.dto.pay.PaymentPreCreateInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static com.share.hy.common.constants.RedisKeyConstant.INFO_KEY_PREFIX;

@Component
public class CacheUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 这里存的是订单完整信息
     * @param id
     * @param tradePlat
     * @return
     */
    public void setPreCreateInfoCache(String id, String tradePlat, String jsonStr, Duration duration) {
        String key = INFO_KEY_PREFIX + tradePlat + ":" + id;
        stringRedisTemplate.opsForValue().set(key, jsonStr, duration);
    }

    public PaymentPreCreateInfoDTO getPreCreateInfoCache(String id, String tradePlat){
        String key = INFO_KEY_PREFIX + tradePlat + ":" + id;
        String s = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(s)) {
            return JSON.parseObject(s, PaymentPreCreateInfoDTO.class);
        }
        return null;
    }


}
