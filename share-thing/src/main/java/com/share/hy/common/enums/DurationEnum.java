package com.share.hy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DurationEnum {

    MONTH((byte)0,31,"包月"),
    QUARTER((byte)1,92,"包季度"),
    YEAR((byte)2,366,"包年");

    private final byte duration;
    private final int day;
    private final String desc;

    public static int getDayByDuration(byte duration){
        for (DurationEnum durationEnum : DurationEnum.values()) {
            if (durationEnum.getDuration() == duration){
                return durationEnum.getDay();
            }
        }
        return 0;
    }

}
