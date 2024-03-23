package com.share.hy.service.pay.trade;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.lumi.aiot.cloud.pay.config.WechatPayConfig;
import com.lumi.aiot.cloud.pay.dto.payment.WechatPayOrderCreateDTO;
import com.wechat.pay.java.service.payments.h5.H5Service;
import com.wechat.pay.java.service.payments.h5.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.AmountReq;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.Refund;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class WechatPayTrader{

    @Autowired
    private H5Service h5Service;

    @Autowired
    private NativePayService nativePayService;

    @Autowired
    private RefundService refundService;

    public String prepayPayByH5(WechatPayOrderCreateDTO wechatPayOrderCreateDTO){
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        //分
        amount.setTotal(wechatPayOrderCreateDTO.getTotal().intValue());
        amount.setCurrency(wechatPayOrderCreateDTO.getCurrency());
        request.setAmount(amount);
        request.setAppid(WechatPayConfig.appId);
        request.setMchid(WechatPayConfig.merchantId);
        request.setDescription(wechatPayOrderCreateDTO.getDesc());
        request.setNotifyUrl(WechatPayConfig.notifyUrl);
        request.setOutTradeNo(wechatPayOrderCreateDTO.getOutTradeNo());
        request.setTimeExpire(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(wechatPayOrderCreateDTO.getTimeExpire()));
        if (StrUtil.isNotBlank(wechatPayOrderCreateDTO.getAttach())) {
            request.setAttach(wechatPayOrderCreateDTO.getAttach());
        }
        SceneInfo sceneInfo = new SceneInfo();
        sceneInfo.setPayerClientIp(wechatPayOrderCreateDTO.getIp());
//        sceneInfo.setDeviceId();
//        sceneInfo.setStoreInfo();
        H5Info h5Info = new H5Info();
        h5Info.setType("Wap");
        sceneInfo.setH5Info(h5Info);

        request.setSceneInfo(sceneInfo);
        PrepayResponse response = h5Service.prepay(request);
        return response.getH5Url();
    }

    public String prepayPayByNative(WechatPayOrderCreateDTO wechatPayOrderCreateDTO){
        com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest request = new com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest();
        com.wechat.pay.java.service.payments.nativepay.model.Amount amount = new com.wechat.pay.java.service.payments.nativepay.model.Amount();
        //分
        amount.setTotal(wechatPayOrderCreateDTO.getTotal().intValue());
        amount.setCurrency(wechatPayOrderCreateDTO.getCurrency());
        request.setAmount(amount);
        request.setAppid(WechatPayConfig.appId);
        request.setMchid(WechatPayConfig.merchantId);
        request.setDescription(wechatPayOrderCreateDTO.getDesc());
        request.setNotifyUrl(WechatPayConfig.notifyUrl);
        request.setOutTradeNo(wechatPayOrderCreateDTO.getOutTradeNo());
        request.setTimeExpire(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(wechatPayOrderCreateDTO.getTimeExpire()));
        if (StrUtil.isNotBlank(wechatPayOrderCreateDTO.getAttach())) {
            request.setAttach(wechatPayOrderCreateDTO.getAttach());
        }
        com.wechat.pay.java.service.payments.nativepay.model.SceneInfo sceneInfo = new com.wechat.pay.java.service.payments.nativepay.model.SceneInfo();
        sceneInfo.setPayerClientIp(wechatPayOrderCreateDTO.getIp());
        request.setSceneInfo(sceneInfo);
        com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse response = nativePayService.prepay(request);
        log.info("response:{}", JSON.toJSONString(response));
        return response.getCodeUrl();
    }

    public Transaction getTransactionByH5AndOrderId(String orderId) {

        QueryOrderByOutTradeNoRequest request1 = new QueryOrderByOutTradeNoRequest();
        request1.setOutTradeNo(orderId);
        request1.setMchid(WechatPayConfig.merchantId);
        return h5Service.queryOrderByOutTradeNo(request1);
    }

    public void closeOrder(String orderId) {
        CloseOrderRequest closeOrderRequest = new CloseOrderRequest();
        closeOrderRequest.setOutTradeNo(orderId);
        closeOrderRequest.setMchid(WechatPayConfig.merchantId);
        h5Service.closeOrder(closeOrderRequest);
    }

    public Refund refund(String orderId,Long total,Long refund,String reason,Integer launchTimes) {
        CreateRequest request = new CreateRequest();
        //request.setSubMchid();
        //request.setTransactionId();
        if (null != launchTimes){
            orderId = orderId + "_" + launchTimes;
        }
        request.setOutTradeNo(orderId);
        request.setOutRefundNo(orderId);
        if (StrUtil.isNotBlank(reason)) {
            request.setReason(reason);
        }
        request.setNotifyUrl(WechatPayConfig.notifyUrl);
        //request.setGoodsDetail();
        AmountReq amount = new AmountReq();
        amount.setRefund(refund);
        //amount.setFrom();
        amount.setTotal(total);
        amount.setCurrency("CNY");

        request.setAmount(amount);
        //request.setFundsAccount();


        return refundService.create(request);
    }


}
