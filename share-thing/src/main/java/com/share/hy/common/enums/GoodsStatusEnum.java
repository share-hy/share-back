package com.share.hy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoodsStatusEnum {

    NORMAL((byte)0,"订购"),
    RENEWAL((byte)1,"续期"),
    UPGRADE((byte)2,"升级"),
    NO_SUPPORT((byte)3,"不支持");

    private final byte code;
    private final String desc;

    public static String getDescByCode(byte code){
        for (GoodsStatusEnum statusEnum : GoodsStatusEnum.values()) {
            if (statusEnum.getCode() == code){
                return statusEnum.getDesc();
            }
        }
        return "";
    }
}
