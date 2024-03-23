package com.share.hy.service.pay.callback;


import com.lumi.aiot.cloud.pay.domain.ShareOrder;
import com.lumi.aiot.cloud.pay.dto.order.PaymentPreCreateInfoDTO;
import com.lumi.aiot.cloud.pay.dto.payment.AlipayEventMsg;

public interface AlipayCallback{
    /**
     * 交易完成。类似于归档的通知。（此时订单无法退款。）
     * @param msg
     * @param order
     */
    void onTradeFinished(AlipayEventMsg msg, ShareOrder order);

    /**
     * 交易部分退款。
     * @param msg
     * @param order
     */
    void onTradeRefund(AlipayEventMsg msg, ShareOrder order);

    /**
     * 交易全额退款。
     * @param msg
     * @param order
     */
    void onTradeRefundFully(AlipayEventMsg msg, ShareOrder order);

    /**
     * 一次性订单交易完成，代表用户的款到账。
     * @param msg
     * @param precreateInfoCache
     */
    void onTradeSuccess(AlipayEventMsg msg, PaymentPreCreateInfoDTO precreateInfoCache);

}
