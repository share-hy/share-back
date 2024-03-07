package com.share.hy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {

    USER((byte)0,"普通用户"),
    ADMIN((byte)1,"管理员");

    private final byte code;
    private final String desc;
}
