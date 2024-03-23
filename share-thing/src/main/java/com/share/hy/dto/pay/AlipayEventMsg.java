package com.share.hy.dto.pay;

import lombok.Data;

import java.math.BigDecimal;

/*
普通订单支付成功示例
{
    "gmt_create":"2023-05-29 15:28:53",
    "charset":"UTF-8",
    "seller_email":"kai.xu-a1949@aqara.com",
    "subject":"1112764262913892352-云存储服务",
    "buyer_id":"2088802454339747",
    "invoice_amount":"0.01",
    "notify_id":"2023052901222152854039741417207358",
    "fund_bill_list":"[{\"amount\":\"0.01\",\"fundChannel\":\"ALIPAYACCOUNT\"}]",
    "notify_type":"trade_status_sync",
    "trade_status":"TRADE_SUCCESS",
    "receipt_amount":"0.01",
    "buyer_pay_amount":"0.01",
    "app_id":"2021003194664455",
    "seller_id":"2088641011272600",
    "gmt_payment":"2023-05-29 15:28:53",
    "notify_time":"2023-05-29 15:28:54",
    "passback_params":"{\"orderType\":0,\"orderId\":\"1112764262913892352\",\"goodsItemId\":\"GI.3\",\"deviceId\":\"virtual.13369554052631\",\"userId\":\"516394621f732177.882303228939956225\"}",
    "version":"1.0",
    "out_trade_no":"1112764262913892352",
    "total_amount":"0.01",
    "trade_no":"2023052922001439741421353853",
    "auth_app_id":"2021003194664455",
    "buyer_logon_id":"228***@qq.com",
    "point_amount":"0.00"
}

普通订单（全额）退款通知示例
{
    "gmt_create": "2023-05-29 15:28:53",
    "charset": "UTF-8",
    "seller_email": "kai.xu-a1949@aqara.com",
    "subject": "1112764262913892352-云存储服务",
    "buyer_id": "2088802454339747",
    "notify_id": "2023052901222153922039741416044044",
    "notify_type": "trade_status_sync",
    "trade_status": "TRADE_CLOSED",
    "app_id": "2021003194664455",
    "seller_id": "2088641011272600",
    "gmt_payment": "2023-05-29 15:28:53",
    "notify_time": "2023-05-29 15:39:22",
    "gmt_refund": "2023-05-29 15:39:22.138",
    "out_biz_no": "1112764262913892352",
    "passback_params": "{\"orderType\":0,\"orderId\":\"1112764262913892352\",\"goodsItemId\":\"GI.3\",\"deviceId\":\"virtual.13369554052631\",\"userId\":\"516394621f732177.882303228939956225\"}",
    "version": "1.0",
    "out_trade_no": "1112764262913892352",
    "total_amount": "0.01",
    "refund_fee": "0.01",
    "trade_no": "2023052922001439741421353853",
    "auth_app_id": "2021003194664455",
    "gmt_close": "2023-05-29 15:39:22",
    "buyer_logon_id": "228***@qq.com"
}


签约扣款成功消息通知
{
    "gmt_create":"2023-05-29 15:54:29",
    "charset":"UTF-8",
    "seller_email":"kai.xu-a1949@aqara.com",
    "subject":"云存储服务",
    "buyer_id":"2088802454339747",
    "invoice_amount":"0.01",
    "notify_id":"2023052901222155430039741415086617",
    "fund_bill_list":"[{\"amount\":\"0.01\",\"fundChannel\":\"ALIPAYACCOUNT\"}]",
    "notify_type":"trade_status_sync",
    "trade_status":"TRADE_SUCCESS",
    "receipt_amount":"0.01",
    "app_id":"2021003194664455",
    "buyer_pay_amount":"0.01",
    "seller_id":"2088641011272600",
    "gmt_payment":"2023-05-29 15:54:30",
    "notify_time":"2023-05-29 15:54:31",
    "version":"1.0",
    "out_trade_no":"1112770950689677312",
    "total_amount":"0.01",
    "trade_no":"2023052922001439741421468018",
    "auth_app_id":"2021003194664455",
    "buyer_logon_id":"228***@qq.com",
    "point_amount":"0.00"
}


签约部分退款消息通知
{
    "gmt_create":"2023-05-29 17:29:12",
    "charset":"UTF-8",
    "seller_email":"kai.xu-a1949@aqara.com",
    "gmt_payment":"2023-05-29 17:29:14",
    "notify_time":"2023-05-29 17:31:05",
    "subject":"云存储服务",
    "gmt_refund":"2023-05-29 17:31:05.287",
    "buyer_id":"2088802454339747",
    "out_biz_no":"1112794784530231296",
    "version":"1.0",
    "notify_id":"2023052901222173105039741416290976",
    "notify_type":"trade_status_sync",
    "out_trade_no":"1112794784530231296",
    "total_amount":"0.03",
    "trade_status":"TRADE_SUCCESS",
    "refund_fee":"0.01",
    "trade_no":"2023052922001439741422051004",
    "auth_app_id":"2021003194664455",
    "buyer_logon_id":"228***@qq.com",
    "app_id":"2021003194664455",
    "seller_id":"2088641011272600"
}}
 */


/**
 * See https://opendocs.alipay.com/open/203/105286?ref=api#%E5%BC%82%E6%AD%A5%E9%80%9A%E7%9F%A5%E5%8F%82%E6%95%B0
 */
@Data
public class AlipayEventMsg {
    private String notify_time;
    private String notify_type;
    private String notify_id;
    private String app_id;
    private String charset;
    private String version;
    /**
     * RSA  || RSA2
     */
    private String sign_type;
    private String sign;
    private String trade_no;
    private String out_trade_no;
    private String out_biz_no;
    private String buyer_id;
    private String buyer_logon_id;
    private String seller_id;
    private String seller_email;
    /**
     * see https://opendocs.alipay.com/open/203/105286?ref=api#%E4%BA%A4%E6%98%93%E7%8A%B6%E6%80%81%E8%AF%B4%E6%98%8E
     */
    private String trade_status;
    private BigDecimal total_amount;
    private BigDecimal receipt_amount;
    private BigDecimal invoice_amount;
    private BigDecimal buyer_pay_amount;
    private BigDecimal point_amount;
    private BigDecimal refund_fee;
    private String subject;
    private String body;
    private String gmt_create;
    private String gmt_payment;
    private String gmt_refund;
    private String gmt_close;
    private String fund_bill_list;
    private String passback_params;
    private String voucher_detail_list;
    private String auth_app_id;
}
