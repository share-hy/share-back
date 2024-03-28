package com.share.hy.service.pay.trade;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.share.hy.common.enums.PaymentPlatEnum;
import com.share.hy.config.AlipayConfig;
import com.share.hy.dto.pay.AlipayOrderCreateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
@Component
public class AlipayTrader implements Trader {

    @Autowired
    private AlipayClient alipayClient;

    @Value("${payment.alipay.quit_url_mapping:{\"0\":\"http://10.11.19.6:8084/#/cloud/payment\"}}")
    private String quitUrlMapping;

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter DF_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    /**
     * 用于返回支付链接
     * @param createDTO
     * @return
     */
    public String createOrder(AlipayOrderCreateDTO createDTO) {
        AlipayTradeWapPayRequest req = new AlipayTradeWapPayRequest();

        req.setNotifyUrl(AlipayConfig.notify_url);
        req.setReturnUrl(AlipayConfig.return_url);
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", createDTO.getOrderId());//内部订单id
        bizContent.put("total_amount", createDTO.getTotalAmount());//总价
        bizContent.put("subject", createDTO.getTitle());//订单(商品）标题
        JSONObject quitUrlMap = JSON.parseObject(quitUrlMapping);
        if (Objects.nonNull(createDTO.getOrderType())) {
            String key = String.valueOf(createDTO.getOrderType().getType());
            if (quitUrlMap.containsKey(key)) {
                bizContent.put("quit_url", quitUrlMap.getString(key));
            }
        }
        String timeExpire = createDTO.getTimeExpire();
        if (StrUtil.isBlank(createDTO.getTimeExpire())) {
            timeExpire = df.format(LocalDateTime.now(ZoneOffset.ofHours(8)).plusMinutes(6));
        }
        bizContent.put("time_expire", timeExpire);
        bizContent.put("passback_params", createDTO.getPassbackParam());//订单(商品）标题
        req.setBizContent(bizContent.toJSONString());
        try {
            return alipayClient.pageExecute(req, "get").getBody();
        } catch (AlipayApiException e) {
            log.error("[Alipay] createOrder. error param:{}", JSON.toJSONString(createDTO), e);
            return null;
        }
    }

    /**
     * 用于返回二维码，用户扫码二维码支付
     * @param createDTO
     * @return
     */
    public String createOrderPagePay(AlipayOrderCreateDTO createDTO) {
        AlipayTradePagePayRequest req = new AlipayTradePagePayRequest();

        req.setNotifyUrl(AlipayConfig.notify_url);
        req.setReturnUrl(AlipayConfig.return_url);
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", createDTO.getOrderId());//内部订单id
        bizContent.put("total_amount", createDTO.getTotalAmount());//总价
        bizContent.put("subject", createDTO.getTitle());//订单(商品）标题
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        JSONObject quitUrlMap = JSON.parseObject(quitUrlMapping);
        if (Objects.nonNull(createDTO.getOrderType())) {
            String key = String.valueOf(createDTO.getOrderType().getType());
            if (quitUrlMap.containsKey(key)) {
                bizContent.put("quit_url", quitUrlMap.getString(key));
            }
        }
        if (StrUtil.isNotBlank(createDTO.getTimeExpire())) {
            bizContent.put("time_expire", createDTO.getTimeExpire());
        }
        //2：目前使用的跳转支付
        bizContent.put("qr_pay_mode", "2");
        //用于定义二维码大小
        bizContent.put("qrcode_width", createDTO.getQrcodeWidth());
        req.setBizContent(bizContent.toJSONString());
        try {
            AlipayTradePagePayResponse response = alipayClient.pageExecute(req, "post");
            return response.getBody();
        } catch (AlipayApiException e) {
            log.error("[Alipay] createOrder. error param:{}", JSON.toJSONString(createDTO), e);
            return null;
        }
    }

    /**
     * 通过内部订单id关闭
     * @param orderId
     * @return
     */
    public AlipayTradeCloseResponse closeOrder(String orderId) {
        AlipayTradeQueryResponse transaction = this.getTransaction(orderId);
        if (Objects.isNull(transaction)) {
            return null;
        }
        if (transaction.getTradeStatus().equals("TRADE_SUCCESS")) {
            log.info("[Alipay] closeOrder. it cannot be closed. because the tradeStatus is TRADE_SUCCESS. orderId:{}", orderId);
            return null;
        }

        AlipayTradeCloseRequest req = new AlipayTradeCloseRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderId);
        bizContent.put("operator_id", "System");
        req.setBizContent(bizContent.toJSONString());
        try {
            return alipayClient.execute(req);
        } catch (AlipayApiException e) {
            log.error("[Alipay] closeOrder. error param:{}", orderId, e);
        }
        return null;
    }

    /**
     * 查询交易状态。
     * @param orderId
     * @return
     */
    public AlipayTradeQueryResponse getTransaction(String orderId) {
        AlipayTradeQueryRequest req = new AlipayTradeQueryRequest ();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderId);
        req.setBizContent(bizContent.toJSONString());
        try {
            AlipayTradeQueryResponse execute = alipayClient.execute(req);
            if (execute.getCode().equals("10000")) {
                return execute;
            }
        } catch (AlipayApiException e) {
            log.error("[Alipay] getTransaction. error param:{}", orderId, e);
        }
        return null;
    }

    /**
     * 查询交易状态。
     *
     * @param orderId
     * @return
     */
    public AlipayTradeFastpayRefundQueryResponse getRefundInfo(String orderId, String out_request_no) {
        AlipayTradeFastpayRefundQueryRequest req = new AlipayTradeFastpayRefundQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderId);
        bizContent.put("out_request_no", orderId);
        if (StrUtil.isNotBlank(out_request_no)) {
            bizContent.put("out_request_no", out_request_no);
        }
        req.setBizContent(bizContent.toJSONString());
        try {
            return alipayClient.execute(req);
        } catch (AlipayApiException e) {
            log.error("[Alipay] getRefundInfo. error param:{}", orderId, e);
        }
        return null;
    }


    public AlipayUserAgreementQueryResponse getAgreementByExternalAgreementNo(String externalAgreementNo) {
        AlipayUserAgreementQueryRequest request = new AlipayUserAgreementQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("personal_product_code", "CYCLE_PAY_AUTH_P");
        bizContent.put("sign_scene", "INDUSTRY|MOBILE");
        bizContent.put("external_agreement_no", externalAgreementNo);
        request.setBizContent(bizContent.toString());
        try {
            AlipayUserAgreementQueryResponse execute = alipayClient.execute(request);
            log.info("[Alipay] getAgreementByExternalAgreementNo. resp:{}",execute);
            return alipayClient.execute(request);
        } catch (AlipayApiException e) {
            log.error("[Alipay] getAgreementByExternalAgreementNo. error param:{}", externalAgreementNo, e);
        }
        return null;
    }

    public AlipayUserAgreementQueryResponse getAgreementByAgreementNo(String agreementNo) {
        AlipayUserAgreementQueryRequest request = new AlipayUserAgreementQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("agreement_no", agreementNo);
        request.setBizContent(bizContent.toString());
        try {
            AlipayUserAgreementQueryResponse execute = alipayClient.execute(request);
            log.info("[Alipay] getAgreementByExternalAgreementNo. resp:{}",execute);
            return execute;
        } catch (AlipayApiException e) {
            log.error("[Alipay] getAgreementByExternalAgreementNo. error param:{}", agreementNo, e);
        }
        return null;
    }

    public AlipayTradePayResponse deductAmount(String agreement_no, String out_trade_no, BigDecimal total_amount) {
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        //异步接收地址，仅支持http/https，公网可访问
        request.setNotifyUrl(AlipayConfig.notify_url);

        /******必传参数******/
        JSONObject bizContent = new JSONObject();
        //商户订单号，商家自定义，保持唯一性
        bizContent.put("out_trade_no", out_trade_no);
        //支付金额，最小值0.01元
        bizContent.put("total_amount", total_amount);
        //订单标题，不可使用特殊符号
        bizContent.put("subject", "云存储服务");
        //周期扣款场景固定传值CYCLE_PAY_AUTH
        bizContent.put("product_code", "CYCLE_PAY_AUTH");

        //协议信息
        JSONObject agreementParams = new JSONObject();
        //周期扣款签约协议后返回的agreement_no
        agreementParams.put("agreement_no", agreement_no);
        bizContent.put("agreement_params", agreementParams);

        request.setBizContent(bizContent.toString());
        AlipayTradePayResponse response = null;
        try {
            return alipayClient.execute(request);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }


    public void modifyExecuteTime(String agreement_no,LocalDate deduct_time,String memo) {
        AlipayUserAgreementExecutionplanModifyRequest  request = new AlipayUserAgreementExecutionplanModifyRequest ();
        JSONObject bizContent = new JSONObject();
        bizContent.put("agreement_no", agreement_no);
        bizContent.put("deduct_time", deduct_time);
        bizContent.put("memo", memo);
        request.setBizContent(bizContent.toString());
        try {
            alipayClient.execute(request);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }

    public AlipayTradeRefundResponse refund(String orderId,String total_amount,String out_request_no) {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setNotifyUrl(AlipayConfig.notify_url);
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderId);
        bizContent.put("refund_amount", total_amount);
        if (StrUtil.isBlank(out_request_no)) {
            bizContent.put("out_request_no", orderId);
        }
        bizContent.put("out_request_no", out_request_no);

        request.setBizContent(bizContent.toString());
        try {
            return alipayClient.execute(request);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getPlatName() {
        return PaymentPlatEnum.ALI_PAY.getCode();
    }
}
