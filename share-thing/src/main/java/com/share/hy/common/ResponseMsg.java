package com.share.hy.common;

import com.share.hy.common.enums.ErrorCodeEnum;
import lombok.*;

/**
 * 返回格式
 */
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Data
public class ResponseMsg<T> {

    /**
     * 状态码，0-成功，1-失败
     */
    private int code;
    /**
     * 请求id
     */
    private String requestId;
    /**
     * msg
     */
    private String message;

    /**
     * 详细的错误码信息,主要用于定位问题,APP上对用户不可见
     */
    private String msgDetails;

    /**
     * 返回业务数据
     */
    private T result;

    public static final ResponseMsg SUCCESS = new ResponseMsg(ErrorCodeEnum.SUCCESS.getCode(), RequestIdHelper.getRequestId(), null, null, null);

    public static ResponseMsg setData(Object data) {
        ResponseMsg res = new ResponseMsg();
        res.setResult(data);
        res.setRequestId(RequestIdHelper.getRequestId());
        return res;
    }
}
