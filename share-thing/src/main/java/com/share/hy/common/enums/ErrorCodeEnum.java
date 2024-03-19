package com.share.hy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {

    SUCCESS(0,"成功","操作成功"),
    PASSWORD_WRONG(101,"密码错误","用户名或者密码错误"),
    ERROR_TOKEN_IS_ABSENCE(102,"缺少Token","token缺失"),
    ERROR_TOKEN_IS_EXPIRED(103,"Token过期","token过期"),
    ERROR_REQUEST_FLOW_LIMIT(104,"操作频繁","操作频繁"),
    ERROR_PERMISSION_DENIED(105,"权限不够","无权限操作"),
    ERROR_ACCOUNT_HAS_REGISTERED(106,"账号已注册","账号已被注册，请更换"),
    ERROR_PARAM_WRONG(107,"参数异常","请求参数异常，请检查"),
    ERROR_SERVER_ERROR(108,"服务异常","服务出小差了，请稍等"),
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
        return ErrorCodeEnum.ERROR_SERVER_ERROR;
    }

}
