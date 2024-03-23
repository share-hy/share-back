package com.share.hy.service.pay.callback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lumi.aiot.cloud.pay.common.enums.CurrencyEnum;
import com.lumi.aiot.cloud.pay.common.enums.PaymentPlatEnum;
import com.lumi.aiot.cloud.pay.domain.ShareOrder;
import com.lumi.aiot.cloud.pay.dto.order.ShareOrderCompletedParam;
import com.lumi.aiot.cloud.pay.dto.order.PaymentPreCreateInfoDTO;
import com.lumi.aiot.cloud.pay.dto.payment.PaymentRefundedParam;
import com.lumi.aiot.cloud.pay.dto.payment.WechatPayRefundEventMsg;
import com.lumi.aiot.cloud.pay.dto.payment.WechatPayTradeEventMsg;
import com.lumi.aiot.cloud.pay.service.OrderService;
import com.lumi.aiot.cloud.pay.service.PayCallbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class WechatPayCallbackImpl implements com.lumi.aiot.cloud.pay.service.trading.callback.wechatpay.WechatPayCallback {

    @Autowired
    private PayCallbackService payCallbackService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onTransactionSuccess(WechatPayTradeEventMsg msg, PaymentPreCreateInfoDTO paymentPreCreateInfo) {
        if (Objects.isNull(paymentPreCreateInfo)) {
            log.warn("[WechatPay] onTransactionSuccess. Not found prepayInfoCache. param:{}", JSON.toJSONString(msg));
            return;
        }

        log.info("start generate deal wechatPay data:{},order create data:{}",msg, JSONObject.toJSONString(paymentPreCreateInfo));
        ShareOrderCompletedParam param = new ShareOrderCompletedParam();
        ShareOrderCompletedParam.OrderInfo orderInfo = new ShareOrderCompletedParam.OrderInfo();
        //String payloadStr = paymentPrecreateInfo.getPayload();
        //AlipayPayload payload = JSON.parseObject(payloadStr, AlipayPayload.class);
        //out_trade_no为orderId +"-"+ launchTimes
        String out_trade_no = msg.getOut_trade_no();
        String[] tradeNo = out_trade_no.split("_");
        String orderId = tradeNo[0];
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent("pay:success:record:" + orderId, System.currentTimeMillis() + "", 30, TimeUnit.MINUTES);
        if (null != success && !success) {
            log.info("had deal the success wechat pay order:{}",orderId);
            return;
        }
        orderInfo.setOrderId(orderId);
        orderInfo.setUserId(paymentPreCreateInfo.getUserId());
        orderInfo.setGoodsItemId(paymentPreCreateInfo.getGoodsItemId());
        orderInfo.setCreateTime(paymentPreCreateInfo.getCreateTime());

        param.setOrderInfo(orderInfo);
        param.setTradeId(msg.getTransaction_id());
        String amountStr = String.valueOf(msg.getAmount().getPayer_total()/100.0);
        param.setTotalAmount(new BigDecimal(amountStr));
        long epochMilli = OffsetDateTime.parse(msg.getSuccess_time(), DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant().toEpochMilli();
        Date payTime = new Date(epochMilli);
        param.setPaymentTime(payTime.getTime());
        param.setCurrency(CurrencyEnum.CNY.getCode());
        param.setTradePlat(PaymentPlatEnum.WECHAT_PAY.getCode());
        param.setRawMsg(msg);

        payCallbackService.orderCompletedWithoutOrder(param);
//        cacheUtil.clearPreCreateOrderCache(orderId, PaymentPlatEnum.WECHAT_PAY.getCode());

    }

    @Override
    public void onRefundSuccess(WechatPayRefundEventMsg msg, ShareOrder order) {
        if (Objects.isNull(order)) {
            log.warn("[WechatPay] onRefundSuccess. Not found order. param:{}", JSON.toJSONString(msg));
            return;
        }
        PaymentRefundedParam param = new PaymentRefundedParam();
        param.setOrder(order);
        param.setTradeId(msg.getTransaction_id());
        param.setTotal(String.valueOf(msg.getAmount().getPayer_refund()/100.0));
        long epochMilli = OffsetDateTime.parse(msg.getSuccess_time(), DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant().toEpochMilli();
        Date refundedTime = new Date(epochMilli);
        param.setRefundedTime(refundedTime.getTime());
        param.setCurrency(CurrencyEnum.CNY.getCode());
        param.setTradePlat(PaymentPlatEnum.WECHAT_PAY.getCode());
        param.setRawMsg(msg);
        orderService.orderRefunded(param);
    }
}
