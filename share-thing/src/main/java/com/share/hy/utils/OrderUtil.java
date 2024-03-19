package com.share.hy.utils;

import cn.hutool.core.util.StrUtil;


public class OrderUtil {
    public static String generateOrderId() {
        return SnowflakeIdWorker.getId();
    }

    public static String getTradeId(String plat,String rawTradeId) {
        if (StrUtil.contains(rawTradeId,"::")) {
            return rawTradeId;
        }
        return plat + "::" + rawTradeId;
    }
}