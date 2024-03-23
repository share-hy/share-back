package com.share.hy.utils;

import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static Date getAssignTime(int distanceDay){
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(System.currentTimeMillis());

        instance.add(Calendar.DAY_OF_YEAR,distanceDay);
        return instance.getTime();
    }
}
