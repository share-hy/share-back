package com.share.hy.service.pay.event;

import com.alibaba.fastjson.JSON;
import com.lumi.aiot.cloud.pay.common.enums.PaymentPlatEnum;
import com.lumi.aiot.cloud.pay.common.enums.WechatPayEnum;
import com.lumi.aiot.cloud.pay.dto.order.PaymentPreCreateInfoDTO;
import com.lumi.aiot.cloud.pay.dto.payment.WechatPayTradeEventMsg;
import com.lumi.aiot.cloud.pay.service.trading.callback.wechatpay.WechatPayCallback;
import com.lumi.aiot.cloud.pay.util.CacheUtil;
import com.share.hy.service.pay.AbstractEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class EventWechatPayTradeSuccessHandler extends AbstractEventHandler<WechatPayTradeEventMsg> {

    @Autowired
    private CacheUtil cacheUtil;

    @Autowired
    private WechatPayCallback wechatPayCallback;

    @Override
    protected WechatPayTradeEventMsg parse(String rawMsg) {
        return JSON.parseObject(rawMsg,WechatPayTradeEventMsg.class);
    }

    @Override
    protected void handleImpl(WechatPayTradeEventMsg msg) {
        //交易成功.
        if (msg.getTrade_state().equals(WechatPayEnum.TradeStatus.SUCCESS.name())) {
            String outTradeNo = msg.getOut_trade_no();
            String[] split = outTradeNo.split("_");
            PaymentPreCreateInfoDTO preCreateInfoCache = cacheUtil.
                    getPreCreateInfoCache(split[0], this.getPlatName());
            if (Objects.isNull(preCreateInfoCache)) {
                log.info("[WechatPay] onTradeSuccess. Not found preCreateInfoCache. param:{}", JSON.toJSONString(msg));
                return;
            }
            wechatPayCallback.onTransactionSuccess(msg,preCreateInfoCache);
        }
    }

    @Override
    public List<String> getEventNames() {
        return Collections.singletonList(
                WechatPayEnum.TradeEvent.TRANSACTION_SUCCESS.getType()
        );
    }

    @Override
    public String getPlatName() {
        return PaymentPlatEnum.WECHAT_PAY.getCode();
    }
}
