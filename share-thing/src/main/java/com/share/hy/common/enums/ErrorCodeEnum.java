package com.share.hy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {

    SUCCESS(0,"成功","操作成功"),
    PASSWORD_WRONG(210,"密码错误","用户名或者密码错误"),
    ;

    private final int code;
    private final String desc;
    private final String message;

    public static ErrorCodeEnum getEnumByCode(int code){
        for (ErrorCodeEnum codeEnum : ErrorCodeEnum.values()) {
            if (codeEnum.getCode() == code){
                return codeEnum;
            }
        }
        return null;
    }

}
