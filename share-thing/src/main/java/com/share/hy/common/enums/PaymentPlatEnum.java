package com.share.hy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum PaymentPlatEnum {
    PAYPAL("PAYPAL"),
    GOOGLE_PLAY("GOOGLE_PLAY"),
    ALI_PAY("ALI_PAY"),
    WECHAT_PAY("WECHAT_PAY"),
    CHINA_PAY_("CHINA_PAY")
    ;

    private final String code;


    public static PaymentPlatEnum getByCode(String code) {
        return map.get(code);
    }


    private static final Map<String, PaymentPlatEnum> map = new HashMap<>();

    static {
        for (PaymentPlatEnum value : values()) {
            map.put(value.getCode(), value);
        }
    }
}
