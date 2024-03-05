package com.share.hy.common;


import org.apache.skywalking.apm.toolkit.trace.TraceContext;


public class RequestIdHelper {

    /**
     *
     * @return
     */
    public static String getRequestId() {
        return TraceContext.traceId();
    }

}
