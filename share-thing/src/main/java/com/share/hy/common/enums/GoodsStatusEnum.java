package com.share.hy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoodsStatusEnum {

    NORMAL((byte)0,"正常"),
    RENEWAL((byte)1,"可续期"),
    UPGRADE((byte)2,"可更换");

    private final byte code;
    private final String desc;
}
