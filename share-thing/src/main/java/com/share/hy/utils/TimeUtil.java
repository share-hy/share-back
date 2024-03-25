package com.share.hy.utils;

import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static Long getAssignTime(int distanceDay){
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(System.currentTimeMillis());

        instance.add(Calendar.DAY_OF_YEAR,distanceDay);
        return instance.getTimeInMillis();
    }

    public static Long getDistanceTime(Date expiredTime, int dayByDuration) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(expiredTime);

        instance.add(Calendar.DAY_OF_YEAR,dayByDuration);
        return instance.getTimeInMillis();
    }
}
