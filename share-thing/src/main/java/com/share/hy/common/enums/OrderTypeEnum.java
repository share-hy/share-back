package com.share.hy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum OrderTypeEnum {

    VPN((byte)0,"VPN服务"),
    ;
    private final Byte type;
    private final String description;


    public static OrderTypeEnum getOrderType(Byte type) {
        return map.get(type);
    }

    private static final Map<Byte, OrderTypeEnum> map = new HashMap<>();

    static {
        for (OrderTypeEnum value : values()) {
            map.put(value.getType(), value);
        }
    }
}
