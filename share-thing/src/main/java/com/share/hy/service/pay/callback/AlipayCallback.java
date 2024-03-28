package com.share.hy.service.pay.callback;


import com.share.hy.domain.ShareOrder;
import com.share.hy.dto.pay.AlipayEventMsg;
import com.share.hy.dto.pay.PaymentPreCreateInfoDTO;

public interface AlipayCallback{
    /**
     * 交易完成。类似于归档的通知。（此时订单无法退款。）
     * @param msg
     * @param order
     */
    void onTradeFinished(AlipayEventMsg msg, ShareOrder order);

    /**
     * 一次性订单交易完成，代表用户的款到账。
     * @param msg
     * @param precreateInfoCache
     */
    void onTradeSuccess(AlipayEventMsg msg, PaymentPreCreateInfoDTO precreateInfoCache);

}
