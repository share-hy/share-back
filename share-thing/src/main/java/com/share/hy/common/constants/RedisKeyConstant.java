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
}
