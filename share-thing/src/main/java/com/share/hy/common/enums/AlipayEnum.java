package com.share.hy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

//https://developer.android.com/google/play/billing/rtdn-reference?hl=zh-cn#sub

public interface AlipayEnum {
    @Getter
    @AllArgsConstructor
    enum TradeEvent{

        TRADE_FINISHED,
        TRADE_SUCCESS,
        WAIT_BUYER_PAY,
        TRADE_CLOSED,
        ;
        private static final Map<String, TradeEvent> map = new HashMap<>();

        static {
            for (TradeEvent value : values()) {
                map.put(value.name(), value);
            }
        }

        public static TradeEvent getByName(String name) {
            return map.get(name);
        }
    }


    @Getter
    @AllArgsConstructor
    enum SignEvent{
        SIGN_COMPLETED,
        UNSIGN_COMPLETED,
        ;
        private static final Map<String, SignEvent> map = new HashMap<>();

        static {
            for (SignEvent value : values()) {
                map.put(value.name(), value);
            }
        }

        public static SignEvent getByName(String name) {
            return map.get(name);
        }
    }

    @Getter
    @AllArgsConstructor
    enum TradeStatus{

        WAIT_BUYER_PAY,
        TRADE_CLOSED,
        TRADE_SUCCESS,
        TRADE_FINISHED,
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
    enum SignStatus{

        NORMAL,
        STOP,
        TEMP,
        UNSIGN,
        ;
        private static final Map<String, SignStatus> map = new HashMap<>();

        static {
            for (SignStatus value : values()) {
                map.put(value.name(), value);
            }
        }

        public static SignStatus getByName(String name) {
            return map.get(name);
        }
    }

}
