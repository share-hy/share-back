package com.share.hy.service.pay.event;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.lumi.aiot.cloud.pay.common.enums.AlipayEnum;
import com.lumi.aiot.cloud.pay.common.enums.PaymentPlatEnum;
import com.lumi.aiot.cloud.pay.domain.ShareOrder;
import com.lumi.aiot.cloud.pay.dto.order.PaymentPreCreateInfoDTO;
import com.lumi.aiot.cloud.pay.dto.payment.AlipayEventMsg;
import com.lumi.aiot.cloud.pay.manager.OrderManager;
import com.lumi.aiot.cloud.pay.service.trading.callback.alipay.AlipayCallback;
import com.lumi.aiot.cloud.pay.util.CacheUtil;
import com.share.hy.service.pay.AbstractEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 此处会处理alipay订单的时时间（包括一次性订单和签约主动代扣的事件。）
 */
@Slf4j
@Component
public class EventAlipayTradeHandler extends AbstractEventHandler<AlipayEventMsg> {

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private CacheUtil cacheUtil;

    @Autowired
    private AlipayCallback alipayCallback;


    @Override
    protected AlipayEventMsg parse(String rawMsg) {
        return JSON.parseObject(rawMsg, AlipayEventMsg.class);
    }

    @Override
    protected void handleImpl(AlipayEventMsg msg) {

        String outTradeNo = msg.getOut_trade_no();
        String[] split = outTradeNo.split("_");
        String orderId = split[0];

        if (msg.getTrade_status().equals(AlipayEnum.TradeStatus.TRADE_FINISHED.name())) {
            ShareOrder order = orderManager.selByOrderId(orderId);
            log.info("[Alipay] onTradeFinished. order:{}",JSON.toJSONString(order));
            if (Objects.isNull(order)) {
                log.info("[Alipay] onTradeFinished. Not found order. param:{}", JSON.toJSONString(msg));
                return;
            }
            alipayCallback.onTradeFinished(msg,order);
        }
        //可能是部分退款，也可能是交易成功(签约和普通订单都会有该事件)。
        else if (msg.getTrade_status().equals(AlipayEnum.TradeStatus.TRADE_SUCCESS.name())) {
            //部分退款
            if (Objects.nonNull(msg.getRefund_fee()) && Objects.nonNull(msg.getGmt_refund())) {
                ShareOrder order = orderManager.selByOrderId(orderId);
                log.info("[Alipay] onTradeRefunded. order:{}",JSON.toJSONString(order));
                if (Objects.isNull(order)) {
                    log.info("[Alipay] onTradeRefunded. Not found order. param:{}", JSON.toJSONString(msg));
                    return;
                }
                alipayCallback.onTradeRefund(msg,order);
                return;
            }

            //交易成功。(一种是主动代扣，一种是一次性订单的precreateInfo.)
            PaymentPreCreateInfoDTO preCreateInfoCache = cacheUtil.
                    getPreCreateInfoCache(orderId, this.getPlatName());
            log.info("[Alipay] onTradeSuccess. preCreateInfoCache:{}", JSON.toJSONString(preCreateInfoCache));
            if (Objects.isNull(preCreateInfoCache)) {
                log.info("[Alipay] onTradeSuccess. Not found preCreateInfoCache. param:{}", JSON.toJSONString(msg));
                return;
            }
            alipayCallback.onTradeSuccess(msg, preCreateInfoCache);

        }
        //超时关闭或者全额退款。
        else if (msg.getTrade_status().equals(AlipayEnum.TradeStatus.TRADE_CLOSED.name())) {
            //全额退款
            if (StrUtil.isNotBlank(msg.getGmt_refund())) {
                ShareOrder order = orderManager.selByOrderId(orderId);
                log.info("[Alipay] onTradeRefundFully. order:{}",JSON.toJSONString(order));
                if (Objects.isNull(order)) {
                    log.info("[Alipay] onTradeRefundFully. Not found order. param:{}", JSON.toJSONString(msg));
                    return;
                }
                alipayCallback.onTradeRefundFully(msg,order);
            }
        }
    }

    @Override
    public List<String> getEventNames() {
        return Arrays.asList(
                AlipayEnum.TradeEvent.TRADE_FINISHED.name(),
                AlipayEnum.TradeEvent.TRADE_SUCCESS.name(),
                //AlipayEnum.TradeEvent.WAIT_BUYER_PAY,
                AlipayEnum.TradeEvent.TRADE_CLOSED.name()
        );
    }

    @Override
    public String getPlatName() {
        return PaymentPlatEnum.ALI_PAY.getCode();
    }
}
