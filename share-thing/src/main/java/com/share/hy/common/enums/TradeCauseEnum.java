package com.share.hy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TradeCauseEnum {

    CONSUME((byte)0,"消费"),
    INVITE_REWARD((byte)1,"邀请奖励"),
    WITHDRAW((byte)2,"提现"),
    RECHARGE((byte)3,"充值");

    private final byte cause;
    private final String desc;

    public static String getDescByCode(byte cause){
        for (TradeCauseEnum causeEnum : TradeCauseEnum.values()) {
            if (causeEnum.getCause() == cause){
                return causeEnum.getDesc();
            }
        }
        return "";
    }
}
