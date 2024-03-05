package com.share.hy.utils;

import com.share.hy.common.HttpCommonHeader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author wxq
 * @date 2018-11-07
 */
@Slf4j
public class SpringRequestHolderUtil {
    public static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }
    public static HttpSession getSession() {
        ServletRequestAttributes requestAttributes = getServletRequestAttributes();
        if (null == requestAttributes) {
            log.warn("get ServletRequestAttributes object is null, probably in an asynchronous thread invoke method. threadName{}", Thread.currentThread().getName());
            return null;
        }
        return requestAttributes.getRequest().getSession();
    }
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = getServletRequestAttributes();
        if (null == requestAttributes) {
            log.warn("get ServletRequestAttributes object is null, probably in an asynchronous thread invoke method. threadName{}", Thread.currentThread().getName());
            return null;
        }
        return requestAttributes.getRequest();
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = getServletRequestAttributes();
        if (null == requestAttributes) {
            log.warn("get ServletRequestAttributes object is null, probably in an asynchronous thread invoke method. threadName{}", Thread.currentThread().getName());
            return null;
        }
        return requestAttributes.getResponse();
    }

    /**
     * 获取header中的Userid
     * @return
     */
    public static String getUserId() {
        HttpServletRequest request = SpringRequestHolderUtil.getRequest();
        if (null == request) {
            log.warn("get ServletRequestAttributes object is null, probably in an asynchronous thread invoke method. threadName{}", Thread.currentThread().getName());
            return "";
        }
        return request.getHeader(HttpCommonHeader.REQUEST_HEADER_USERID);
    }

    public static String getLang() {
        HttpServletRequest request = SpringRequestHolderUtil.getRequest();
        if (null == request) {
            log.warn("get ServletRequestAttributes object is null, probably in an asynchronous thread invoke method. threadName{}", Thread.currentThread().getName());
            return "";
        }
        return request.getHeader(HttpCommonHeader.REQUEST_HEADER_LANG);
    }

}
