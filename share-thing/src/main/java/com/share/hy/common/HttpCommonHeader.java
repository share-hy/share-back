package com.share.hy.common;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Data
public class HttpCommonHeader {

    public static final String REQUEST_HEADER_NONCE = "Nonce";

    public static final String REQUEST_HEADER_SIGN = "Sign";

    public static final String REQUEST_HEADER_TOKEN = "Token";

    public static final String REQUEST_HEADER_USERID = "Userid";

    public static final String REQUEST_HEADER_TIME = "Time";

    public static final String REQUEST_HEADER_LANG = "Lang";

    private String nonce;
    private String sign;
    private String token;
    private String time;
    private String lang;
    private String userId;
    private Map<String, String> paramsMap;

    public void initMap() {
        this.paramsMap.put("Sign", this.sign);
        this.paramsMap.put("Token", this.token);
        this.paramsMap.put("Userid", this.userId);
        this.paramsMap.put("Time", this.time);
        this.paramsMap.put("Lang", this.lang);
        this.paramsMap.put("Nonce", this.nonce);
    }

    public HttpCommonHeader(HttpServletRequest request) {
        this.paramsMap = new HashMap();
        this.sign = request.getHeader("Sign");
        this.token = request.getHeader("Token");
        this.userId = request.getHeader("Userid");
        this.lang = request.getHeader("Lang");
        this.time = request.getHeader("Time");
        this.nonce = request.getHeader("Nonce");
        this.initMap();
    }

}
