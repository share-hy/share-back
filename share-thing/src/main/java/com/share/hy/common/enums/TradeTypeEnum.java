package com.share.hy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum TradeTypeEnum {
    INCOME((byte)0,"收入"),
    EXPENDITURES ((byte)1,"支出"),
            ;
    private final Byte type;
    private final String description;

    private static final Map<Byte, TradeTypeEnum> map = new HashMap<>();

    static {
        for (TradeTypeEnum value : values()) {
            map.put(value.type, value);
        }
    }

    public static TradeTypeEnum getByType(Byte type) {
        return map.get(type);
    }

}
