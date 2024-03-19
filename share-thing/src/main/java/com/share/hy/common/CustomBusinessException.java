package com.share.hy.common;

import com.share.hy.common.enums.ErrorCodeEnum;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @Auther: hongsheng.wei
 * @Date: 2018/10/26
 * @Description: 自定义业务异常类
 */
@Getter
public class CustomBusinessException extends RuntimeException{

    /**
     * 用于返回给前端定位问题用，不是用于弹窗提示的消息
     */
    private String errorMsgDetails;
    
    private ErrorCodeEnum errorCodeEnumCommon;

    public CustomBusinessException(ErrorCodeEnum errorCodeEnumCommon) {
        super(errorCodeEnumCommon.getDesc());
        this.errorCodeEnumCommon = errorCodeEnumCommon;
    }

    public CustomBusinessException(int code) {
        super(ErrorCodeEnum.getEnumByCode(code).getDesc());
        this.errorCodeEnumCommon = ErrorCodeEnum.getEnumByCode(code);
    }

    public CustomBusinessException(ErrorCodeEnum errorCodeEnumCommon, String replaceErrorDesc) {
        super(StringUtils.isBlank(replaceErrorDesc) ? errorCodeEnumCommon.getDesc() : replaceErrorDesc);
        this.errorCodeEnumCommon = errorCodeEnumCommon;
    }

    public CustomBusinessException(String errorMsgNoPromptUser, ErrorCodeEnum errorCodeEnumCommon) {
        this(errorCodeEnumCommon);
        this.errorMsgDetails = errorMsgNoPromptUser;
    }

    public <T> CustomBusinessException(T... paramsErrorMsg) {
        this(ErrorCodeEnum.PASSWORD_WRONG);
        StringBuilder stringBuilder = new StringBuilder();
        for (T errorMsg : paramsErrorMsg) {
            stringBuilder.append(errorMsg);
        }
        this.errorMsgDetails = stringBuilder.toString();
    }

}
