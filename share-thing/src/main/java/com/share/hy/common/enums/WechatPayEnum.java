package com.share.hy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


public interface WechatPayEnum {
    @Getter
    @AllArgsConstructor
    enum TradeEvent{

        REFUND_SUCCESS("REFUND.SUCCESS"),
        TRANSACTION_SUCCESS("TRANSACTION.SUCCESS"),
        ;

        private final String type;

        private static final Map<String, TradeEvent> map = new HashMap<>();

        static {
            for (TradeEvent value : values()) {
                map.put(value.getType(), value);
            }
        }

        public static TradeEvent getByType(String type) {
            return map.get(type);
        }
    }

    //
    //@Getter
    //@AllArgsConstructor
    //enum SignEvent{
    //    SIGN_COMPLETED,
    //    UNSIGN_COMPLETED,
    //    ;
    //    private static final Map<String, SignEvent> map = new HashMap<>();
    //
    //    static {
    //        for (SignEvent value : values()) {
    //            map.put(value.name(), value);
    //        }
    //    }
    //
    //    public static SignEvent getByName(String name) {
    //        return map.get(name);
    //    }
    //}

    @Getter
    @AllArgsConstructor
    enum TradeStatus{

        SUCCESS,
        REFUND,
        NOTPAY,
        CLOSED,
        REVOKED,
        USERPAYING,
        PAYERROR,
        ;
        private static final Map<String, TradeStatus> map = new HashMap<>();

        static {
            for (TradeStatus value : values()) {
                map.put(value.name(), value);
            }
        }

        public static TradeStatus getByName(String name) {
            return map.get(name);
        }
    }


    @Getter
    @AllArgsConstructor
    enum RefundStatus{

        SUCCESS,
        CLOSED,
        ABNORMAL,
        ;
        private static final Map<String, RefundStatus> map = new HashMap<>();

        static {
            for (RefundStatus value : values()) {
                map.put(value.name(), value);
            }
        }

        public static RefundStatus getByName(String name) {
            return map.get(name);
        }
    }
    //
    //@Getter
    //@AllArgsConstructor
    //enum SignStatus{
    //
    //    NORMAL,
    //    STOP,
    //    TEMP,
    //    UNSIGN,
    //    ;
    //    private static final Map<String, SignStatus> map = new HashMap<>();
    //
    //    static {
    //        for (SignStatus value : values()) {
    //            map.put(value.name(), value);
    //        }
    //    }
    //
    //    public static SignStatus getByName(String name) {
    //        return map.get(name);
    //    }
    //}

}
