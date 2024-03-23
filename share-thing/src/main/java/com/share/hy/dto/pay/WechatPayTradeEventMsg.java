package com.share.hy.dto.pay;

import lombok.Data;

@Data
public class WechatPayTradeEventMsg {
    private String transaction_id;
    private Amount amount;
    private String mchid;
    private String out_trade_no;
    private String trade_state;
    private String bank_type;
    private String appid;
    private String trade_state_desc;
    private String trade_type;
    private String attach;
    private String success_time;

    /*
    "payer":{
        "openid":"oGUIo0rRkPMLyFJ0gyB23i65iPbA"
    }
     */
    //private String payer;
    @Data
    public static class Amount {
        private Long total;
        private Long payer_total;
        private String currency;
        private String payer_currency;
    }
}
