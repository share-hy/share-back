package com.share.hy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  ServiceStatusEnum {
    
    //服务类型 0-到期 1-正常使用 2-主动停止，升降级服务
    EXPIRE((byte)0,"过期"),
    NORMAL((byte)1,"正常使用"),
    UPGRADE((byte)2,"服务升降级")

    ;
    private final byte status;
    private final String desc;
}
