package com.share.hy.service.pay.callback;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.share.hy.common.enums.PaymentPlatEnum;
import com.share.hy.common.enums.WechatPayEnum;
import com.share.hy.config.AlipayConfig;
import com.share.hy.dto.pay.AlipayAttachmentDTO;
import com.share.hy.dto.pay.AlipayEventMsg;
import com.share.hy.dto.pay.PaymentPreCreateInfoDTO;
import com.share.hy.dto.pay.WechatPayTradeEventMsg;
import com.share.hy.service.pay.trade.AlipayTrader;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.model.TransactionAmount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.SendResult;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class ResendMsgService {

    @Autowired
    private AlipayTrader alipayTrader;

    public void sendAlipayTradeSuccessMsg(String queryId, PaymentPreCreateInfoDTO prepayInfoCache, AlipayTradeQueryResponse transaction)  {
        /*
        {
    "gmt_create":"2023-05-29 15:28:53",
    "charset":"UTF-8",
    "seller_email":"//TODO",
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
         */
        Date now = new Date();
        AlipayTradeFastpayRefundQueryResponse refundInfo = alipayTrader.getRefundInfo(queryId, null);
        log.info("refundInfo is :{}, id is : {}", refundInfo, queryId);
        if ("REFUND_SUCCESS".equals(refundInfo.getRefundStatus())) {
            log.info("order has refunded. queryId:{}",queryId);
        }

        AlipayEventMsg alipayEventMsg = new AlipayEventMsg();
        alipayEventMsg.setGmt_create(DateUtil.format(now, "yyyy-MM-dd HH:mm:ss"));
        alipayEventMsg.setCharset("UTF-8");
//                alipayEventMsg.setSeller_email();
        alipayEventMsg.setSubject(transaction.getSubject()+"Manually");
        alipayEventMsg.setBuyer_id(transaction.getBuyerUserId());
        alipayEventMsg.setInvoice_amount(new BigDecimal(transaction.getInvoiceAmount()));
        alipayEventMsg.setNotify_id(UUID.randomUUID().toString());
        alipayEventMsg.setFund_bill_list(JSON.toJSONString(transaction.getFundBillList()));
        alipayEventMsg.setNotify_type("trade_status_sync");
        alipayEventMsg.setTrade_status(transaction.getTradeStatus());
        alipayEventMsg.setReceipt_amount(new BigDecimal(transaction.getReceiptAmount()));
        alipayEventMsg.setBuyer_pay_amount(new BigDecimal(transaction.getBuyerPayAmount()));
        alipayEventMsg.setApp_id(AlipayConfig.APPID);
//                alipayEventMsg.setSeller_id();
        alipayEventMsg.setGmt_payment(DateUtil.format(transaction.getSendPayDate(), "yyyy-MM-dd HH:mm:ss"));
        alipayEventMsg.setNotify_time(DateUtil.format(now, "yyyy-MM-dd HH:mm:ss"));
        AlipayAttachmentDTO alipayAttachmentDTO = new AlipayAttachmentDTO();
        alipayAttachmentDTO.setOrderId(queryId);
        alipayAttachmentDTO.setUserId(prepayInfoCache.getUserId());
        alipayAttachmentDTO.setGoodsItemId(prepayInfoCache.getGoodsItemId());
        alipayAttachmentDTO.setOrderType(prepayInfoCache.getOrderType());
        alipayEventMsg.setPassback_params(JSON.toJSONString(alipayAttachmentDTO));
        alipayEventMsg.setVersion("1.0 - manually confirm");
        alipayEventMsg.setOut_trade_no(queryId);
        alipayEventMsg.setTotal_amount(new BigDecimal(transaction.getTotalAmount()));
        alipayEventMsg.setTrade_no(transaction.getTradeNo());
        //alipayEventMsg.setAuth_app_id();
        alipayEventMsg.setBuyer_logon_id(transaction.getBuyerLogonId());
        alipayEventMsg.setPoint_amount(new BigDecimal(transaction.getPointAmount()));
//                alipayEventMsg.setSign_type("RSA2");
//                alipayEventMsg.setSign();

        String tags = PaymentPlatEnum.ALI_PAY.getCode() + "::" + transaction.getTradeStatus();
        //立刻发送一条支付成功的消息，避免第三方回调太慢进而导致服务生效慢。
        SendResult sendResult = rocketMqProducer.syncSendForResult(RocketMqTopicConstant.TOPIC_PAY_PAYMENT_EVENT, tags, alipayEventMsg);
        log.info("sendMsgRes is :{}, id is : {}", sendResult, queryId);
    }



    public void sendWechatPayTradeSuccessMsg(Transaction transaction) {
        WechatPayTradeEventMsg wechatPayTradeEventMsg = new WechatPayTradeEventMsg();
        wechatPayTradeEventMsg.setTransaction_id(transaction.getTransactionId());
        TransactionAmount amount = transaction.getAmount();
        WechatPayTradeEventMsg.Amount amount1 = new WechatPayTradeEventMsg.Amount();
        amount1.setTotal(amount.getTotal().longValue());
        amount1.setPayer_total(amount.getPayerTotal().longValue());
        amount1.setCurrency(amount.getCurrency());
        amount1.setPayer_currency(amount.getPayerCurrency());

        wechatPayTradeEventMsg.setAmount(amount1);
        wechatPayTradeEventMsg.setMchid(transaction.getMchid());
        wechatPayTradeEventMsg.setOut_trade_no(transaction.getOutTradeNo());
        wechatPayTradeEventMsg.setTrade_state(transaction.getTradeState().name());
        wechatPayTradeEventMsg.setBank_type(transaction.getBankType());
        wechatPayTradeEventMsg.setAppid(transaction.getAppid());
        wechatPayTradeEventMsg.setTrade_state_desc(transaction.getTradeStateDesc());
        wechatPayTradeEventMsg.setTrade_type(transaction.getTradeType().name());
        wechatPayTradeEventMsg.setAttach(transaction.getAttach());
        wechatPayTradeEventMsg.setSuccess_time(transaction.getSuccessTime());


        String tags = PaymentPlatEnum.WECHAT_PAY.getCode() + "::" + WechatPayEnum.TradeEvent.TRANSACTION_SUCCESS.getType();
        SendResult sendResult = rocketMqProducer.syncSendForResult(RocketMqTopicConstant.TOPIC_PAY_PAYMENT_EVENT, tags, wechatPayTradeEventMsg);
        log.info("sendMsgRes is :{}, id is : {}", sendResult, transaction.getOutTradeNo());
    }

}
