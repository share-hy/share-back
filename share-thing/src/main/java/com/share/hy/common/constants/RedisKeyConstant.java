package com.share.hy.common.constants;

public class RedisKeyConstant {

    private static final String USER_TOKEN_KEY = "share:user:token:";

    private static final String CONSOLE_LIMIT = "share:console:limit:";

    public static String getUserTokenKey(String token){
        return USER_TOKEN_KEY + token;

    }
    public static String getConsoleLimit(String userId){
        return CONSOLE_LIMIT + userId;
    }

    private static final String PRE_CREATE_INFO_PREFIX = "pre_create_info:";
    public static final String PAY_REDIS_KEY = "pay:";
    public static final String INFO_KEY_PREFIX = RedisKeyConstant.PAY_REDIS_KEY + PRE_CREATE_INFO_PREFIX;
}
