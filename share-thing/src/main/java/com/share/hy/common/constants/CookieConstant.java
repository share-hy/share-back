package com.share.hy.common.constants;


import com.share.hy.utils.SpringRequestHolderUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by yangyanchen on 2021/3/1
 */
public class CookieConstant {

    public static final int LOGIN_EXPIRED_TIME = 10800;
    public static final String TOKEN_COOKIE_NAME = "Token";
    public static final String COOKIE_USER_ID = "Userid";


    private static void addCookie(String cookieKey, String cookieValue,String domain, int secondTimeout) {
        addCookie(cookieKey, cookieValue,domain, secondTimeout, false);
    }


    private static void addCookie(String cookieKey,String cookieValue,String domain,int secondTimeout,boolean httpOnly){
        Cookie cookie = new Cookie(cookieKey, cookieValue);
        cookie.setHttpOnly(httpOnly);
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setMaxAge(secondTimeout);
        SpringRequestHolderUtil.getResponse().addCookie(cookie);
    }

    /**
     * 添加token cookie
     *
     * @param cookieValue
     * @param secondTimeout
     */
    public static void addTokenCookie(String domain,String cookieValue, int secondTimeout) {
        addCookie(CookieConstant.TOKEN_COOKIE_NAME,cookieValue,domain, secondTimeout);
    }

    public static void addUserIdCookie(String domain,String cookieValue, int secondTimeout) {
        addCookie(CookieConstant.COOKIE_USER_ID,cookieValue,domain, secondTimeout);
    }



    /**
     * 获取cookie值
     *
     * @param cookieName
     * @return
     */
    public static String getCookieValue(String cookieName) {
        return getCookieValue(SpringRequestHolderUtil.getRequest(), cookieName);
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        if (null == cookieName) {
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            return null;
        }
        String cookieValue = null;
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                cookieValue = cookie.getValue();
                break;
            }
        }

        return cookieValue;
    }
}
