package com.share.hy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum CurrencyEnum {
    USD("USD","$"),
    CNY("CNY","¥"),
    EUR("EUR","€"),
    GBP("GBP","￡"),
    RUB("RUB","₽"),
    INR("INR","₹"),
    KRW("KRW","₩"),

    ;

    private static final Map<String, String> map = new HashMap<>();

    static {
        for (CurrencyEnum value : values()) {
            map.put(value.code, value.symbol);
        }
    }

    public static String getSymbol(String code) {
        return map.get(code);
    }

    private final String code;
    private final String symbol;
}
