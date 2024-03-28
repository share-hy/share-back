package com.share.hy.utils;


import io.netty.util.HashedWheelTimer;

import java.util.concurrent.TimeUnit;

public class HashedWheelTimerUtils {

    private static final HashedWheelTimer sendTimer = new HashedWheelTimer(1,TimeUnit.SECONDS,10);

    public static HashedWheelTimer getSendTimer(){
        return sendTimer;
    }
}
