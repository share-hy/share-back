package com.share.hy.service.pay.callback;

import com.alibaba.fastjson.JSON;
import com.share.hy.common.enums.CurrencyEnum;
import com.share.hy.common.enums.PaymentPlatEnum;
import com.share.hy.domain.ShareOrder;
import com.share.hy.dto.pay.AlipayEventMsg;
import com.share.hy.dto.pay.AlipayPayload;
import com.share.hy.dto.pay.PaymentOrderCompletedParam;
import com.share.hy.dto.pay.PaymentPreCreateInfoDTO;
import com.share.hy.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 各系统接收消息通知，自行处理
 */
@Slf4j
@Component
public class AlipayCloudCallbackImpl implements AlipayCallback {

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DF_WITH_MICROSECOND = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Autowired
    private IOrderService orderService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private PayCallbackService payCallbackService;


    @Override
    public void onTradeFinished(AlipayEventMsg msg, ShareOrder order) {
        //Needn't to deal.
        log.info("[Alipay] onTradeFinished. param:{}", JSON.toJSONString(msg));
    }

    /**
     * 注意！！！！！！ 有两种情况下会触发此事件 。
     * 1.交易成功。
     * 2.部分退款。
     * 如何区分部分退款和交易成功？ https://opendocs.alipay.com/support/01rawd?pathHash=535bc862
     *
     * @param msg
     */
    @Override
    public void onTradeSuccess(AlipayEventMsg msg, PaymentPreCreateInfoDTO paymentPreCreateInfo) {
        if (Objects.isNull(paymentPreCreateInfo)) {
            log.warn("[Alipay] onTradeSuccess. Not found prepayInfoCache. param:{}", JSON.toJSONString(msg));
            return;
        }

        PaymentOrderCompletedParam param = new PaymentOrderCompletedParam();
        PaymentOrderCompletedParam.OrderInfo orderInfo = new PaymentOrderCompletedParam.OrderInfo();
        String payloadStr = paymentPreCreateInfo.getPayload();
        AlipayPayload payload = JSON.parseObject(payloadStr, AlipayPayload.class);
        String out_trade_no = payload.getOrder().getOut_trade_no();
        String[] orderTimes= out_trade_no.split("_");
        String orderId = orderTimes[0];
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent("pay:success:record:" + orderId, System.currentTimeMillis() + "", 30, TimeUnit.MINUTES);
        if (null != success && !success) {
            log.info("had deal the success alipay order:{}",orderId);
            return;
        }
        orderInfo.setOrderId(orderId);
        orderInfo.setUserId(paymentPreCreateInfo.getUserId());
        orderInfo.setGoodsItemId(paymentPreCreateInfo.getGoodsItemId());
        orderInfo.setCreateTime(paymentPreCreateInfo.getCreateTime());

        param.setOrderInfo(orderInfo);
        param.setTradeId(msg.getTrade_no());
        param.setTotalAmount(msg.getTotal_amount());
        Date payTime = new Date(LocalDateTime.parse(msg.getGmt_payment(), DF).toEpochSecond(ZoneOffset.ofHours(8)) * 1000);
        param.setPaymentTime(payTime.getTime());
        param.setCurrency(CurrencyEnum.CNY.getCode());
        param.setTradePlat(PaymentPlatEnum.ALI_PAY.getCode());
        param.setRawMsg(msg);

        payCallbackService.orderCompletedWithoutOrder(param);
    }

}
