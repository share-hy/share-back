package com.share.hy.common.constants;

import com.share.hy.utils.SnowflakeIdWorker;

import java.util.Random;

public class UserConstant {

    private static String base16Letter = "00112233445566887799abc1230456987def0123456789";
    private static final int LENGTH_USERID = 34;

    /**
     * 生成userId
     */
    public static String generateUserId() {
        Long snowId = SnowflakeIdWorker.nextId();
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LENGTH_USERID - snowId.toString().length(); i++) {
            int number = random.nextInt(base16Letter.length());
            sb.append(base16Letter.charAt(number));
        }
        return sb.append(".").toString() + SnowflakeIdWorker.nextId();
    }
}
