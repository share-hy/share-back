package com.share.hy.service.pay.event;

import com.alibaba.fastjson.JSON;
import com.lumi.aiot.cloud.pay.common.enums.PaymentPlatEnum;
import com.lumi.aiot.cloud.pay.common.enums.WechatPayEnum;
import com.lumi.aiot.cloud.pay.domain.ShareOrder;
import com.lumi.aiot.cloud.pay.dto.payment.WechatPayRefundEventMsg;
import com.lumi.aiot.cloud.pay.manager.OrderManager;
import com.lumi.aiot.cloud.pay.service.trading.callback.wechatpay.WechatPayCallback;
import com.share.hy.service.pay.AbstractEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class EventWechatPayRefundSuccessHandler extends AbstractEventHandler<WechatPayRefundEventMsg> {

    @Autowired
    private OrderManager orderManager;

    @Autowired
    WechatPayCallback wechatPayCallback;

    @Override
    protected WechatPayRefundEventMsg parse(String rawMsg) {
        return JSON.parseObject(rawMsg, WechatPayRefundEventMsg.class);
    }

    @Override
    protected void handleImpl(WechatPayRefundEventMsg msg) {
        String outTradeNo = msg.getOut_trade_no();
        String[] split = outTradeNo.split("_");
        //退款成功.
        if (msg.getRefund_status().equals(WechatPayEnum.RefundStatus.SUCCESS.name())) {
            ShareOrder order = orderManager.selByOrderId(split[0]);
            if (Objects.isNull(order)) {
                log.info("[WechatPay] onRefunduccess. Not found order. param:{}", JSON.toJSONString(msg));
                return;
            }
            wechatPayCallback.onRefundSuccess(msg,order);
        }
    }

    @Override
    public List<String> getEventNames() {
        return Collections.singletonList(
                WechatPayEnum.TradeEvent.REFUND_SUCCESS.getType()
        );
    }

    @Override
    public String getPlatName() {
        return PaymentPlatEnum.WECHAT_PAY.getCode();
    }
}
