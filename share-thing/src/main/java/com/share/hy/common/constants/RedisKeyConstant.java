package com.share.hy.common.constants;

public class RedisKeyConstant {

    private static final String USER_TOKEN_KEY = "thing:user:token:";

    public static String getUserTokenKey(String token){
        return USER_TOKEN_KEY + token;
    }
}
