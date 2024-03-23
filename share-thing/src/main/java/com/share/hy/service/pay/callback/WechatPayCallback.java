package com.share.hy.service.pay.callback;


import com.lumi.aiot.cloud.pay.domain.ShareOrder;
import com.lumi.aiot.cloud.pay.dto.order.PaymentPreCreateInfoDTO;
import com.lumi.aiot.cloud.pay.dto.payment.WechatPayRefundEventMsg;
import com.lumi.aiot.cloud.pay.dto.payment.WechatPayTradeEventMsg;

public interface WechatPayCallback{
    void onTransactionSuccess(WechatPayTradeEventMsg msg, PaymentPreCreateInfoDTO precreateInfoCache);
    void onRefundSuccess(WechatPayRefundEventMsg msg, ShareOrder order);
}
