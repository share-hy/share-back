package com.share.hy.common.controller;

import com.alibaba.fastjson.JSON;
import com.share.hy.common.RequestIdHelper;
import com.share.hy.common.ResponseMsg;
import com.share.hy.common.enums.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller公共类
 */
@Component
@Slf4j
public class BaseController {

    /**
     * error result
     *
     * @return
     */
    protected ResponseMsg success() {
        ResponseMsg responseMsg = getResult(ErrorCodeEnum.SUCCESS);
        return responseMsg;
    }


    protected ResponseMsg success(Object data) {
        ResponseMsg responseMsg = getResult(ErrorCodeEnum.SUCCESS, data);
        return responseMsg;
    }

    protected ResponseMsg success(String key, Object data) {
        Map<String, Object> map = new HashMap<>(1, 1);
        map.put(key, data);
        return success(map);
    }


    protected ResponseMsg failed(ErrorCodeEnum errorCodeEnumCommon) {
        ResponseMsg responseMsg = getResult(errorCodeEnumCommon);
        return responseMsg;
    }

    protected ResponseMsg failed(ErrorCodeEnum errorCodeEnumCommon, Object msgDetails) {
        return getResult(errorCodeEnumCommon, msgDetails);
    }

    protected ResponseMsg failed(int code) {
        return failed(ErrorCodeEnum.getEnumByCode(code));
    }

    protected ResponseMsg failed(int code, Object msgDetails) {
        return failed(ErrorCodeEnum.getEnumByCode(code), msgDetails);
    }

    /**
     * 如果为成功data为成功结果，如果为失败，data为失败信息
     *
     * @param errorCodeEnum
     * @param data
     * @return
     */
    protected ResponseMsg getResult(ErrorCodeEnum errorCodeEnum, Object data) {
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setRequestId(RequestIdHelper.getRequestId());
        responseMsg.setCode(errorCodeEnum.getCode());
        responseMsg.setMessage(errorCodeEnum.getDesc());
        if (null == data) {
            data = "";
        }

        if (errorCodeEnum.getCode() != ErrorCodeEnum.SUCCESS.getCode()) {
            responseMsg.setMsgDetails(data.toString());
        }
        responseMsg.setResult(data);

        log.info("--ResponseMsg--:{},code:{}", JSON.toJSONString(responseMsg),responseMsg.getCode());

        return responseMsg;
    }

    /**
     * 如果为成功data为成功结果，如果为失败，data为失败信息
     *
     * @param code
     * @param desc
     * @param data
     * @return
     */
    protected ResponseMsg getResult(int code, String desc, Object data) {
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setRequestId(RequestIdHelper.getRequestId());
        responseMsg.setCode(code);
        responseMsg.setMessage(desc);
        if (null == data) {
            data = "";
        }
        if (code != ErrorCodeEnum.SUCCESS.getCode()) {
            responseMsg.setMsgDetails(data.toString());
        }
        responseMsg.setResult(data);
        log.info("--ResponseMsg--:{},code:{}", JSON.toJSONString(responseMsg),code);
        return responseMsg;
    }


    protected ResponseMsg getResultOfResultCouldBeNull(ErrorCodeEnum errorCodeEnumCommon, Object result) {
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setRequestId(RequestIdHelper.getRequestId());
        responseMsg.setCode(errorCodeEnumCommon.getCode());
        responseMsg.setMessage(errorCodeEnumCommon.getDesc());
        responseMsg.setResult(result);

        HttpServletRequest request = getRequest();
        // 为了简化日志查询,POST请求才打印详细返回日志 Get只打印code
        if (request.getMethod().equalsIgnoreCase(RequestMethod.POST.name())) {
            log.info("--ResponseMsg--:{}", JSON.toJSONString(responseMsg));
        } else {
            log.info("--ResponseMsg--:code:{}", responseMsg.getCode());
        }

        return responseMsg;
    }
    protected static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }


    protected ResponseMsg getResult(ErrorCodeEnum errorCodeEnumCommon) {
        return getResult(errorCodeEnumCommon, "");
    }

    protected ResponseMsg getNullResult(ErrorCodeEnum errorCodeEnum) {
        return getResultOfResultCouldBeNull(errorCodeEnum, null);
    }



}
