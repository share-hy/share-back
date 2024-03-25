package com.share.hy.utils;

import cn.hutool.core.date.DateTime;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

//    /**
//     * 获取明天凌晨0点时刻
//     */
//    public static Long getTomorrowDay0h() {
//        Calendar instance = Calendar.getInstance();
//        instance.setTime(new DateTime());
//
//        instance.add(Calendar.DAY_OF_YEAR,1);
//        instance.set(Calendar.HOUR_OF_DAY,0);
//        instance.set(Calendar.MINUTE,0);
//        instance.set(Calendar.SECOND,0);
//        instance.set(Calendar.MILLISECOND,0);
//        return instance.getTimeInMillis();
//    }

    /**
     * 计算endTime距currentTime多少天
     * @param endTime
     * @param currentTime
     * @return
     */
    public static int calculateGap(Date endTime,Date currentTime) {
        Calendar endTimeCalendar = Calendar.getInstance();
        endTimeCalendar.setTime(endTime);
        int endYear = endTimeCalendar.get(Calendar.YEAR);
        int endDay = endTimeCalendar.get(Calendar.DAY_OF_MONTH);
        int endMonth = endTimeCalendar.get(Calendar.MONTH);


        Calendar currentTimeCalendar = Calendar.getInstance();
        currentTimeCalendar.setTime(currentTime);
        int currentYear = currentTimeCalendar.get(Calendar.YEAR);
        int currentDay = currentTimeCalendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = currentTimeCalendar.get(Calendar.MONTH);

        LocalDate end = LocalDate.of(endYear, endMonth + 1, endDay); // 起始日期
        LocalDate current = LocalDate.of(currentYear,currentMonth + 1,currentDay); // 结束日期（当前日期）

        long between = ChronoUnit.DAYS.between(current,end);

        return (int)between;
    }
}
