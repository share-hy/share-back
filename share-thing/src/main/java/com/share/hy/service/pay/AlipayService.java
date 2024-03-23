package com.share.hy.service.pay;

import com.alibaba.fastjson.JSON;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayUserAgreementQueryResponse;
import com.share.hy.common.CustomBusinessException;
import com.share.hy.common.enums.AlipayEnum;
import com.share.hy.common.enums.PaymentPlatEnum;
import com.share.hy.domain.ShareOrder;
import com.share.hy.dto.pay.*;
import com.share.hy.service.pay.PaymentService;
import com.share.hy.service.pay.callback.ResendMsgService;
import com.share.hy.service.pay.trade.AlipayTrader;
import com.share.hy.utils.CacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class AlipayService implements PaymentService {

    @Autowired
    private AlipayTrader alipayTrader;

    @Autowired
    private CacheUtil cacheUtil;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private ResendMsgService resendMsgService;

    @Override
    public String launchPay(LaunchPayDTO launchPayDTO) {
        AlipayOrderCreateDTO alipayOrderCreateDTO = new AlipayOrderCreateDTO();
        alipayOrderCreateDTO.setOrderId(launchPayDTO.getOutTradeNo());
        alipayOrderCreateDTO.setTotalAmount(launchPayDTO.getRawPrice().toPlainString());
        alipayOrderCreateDTO.setTitle(launchPayDTO.getOrderId() +"-"+ launchPayDTO.getTypeEnum().getType());
        AlipayAttachmentDTO passbackParam = new AlipayAttachmentDTO();
        passbackParam.setOrderId(launchPayDTO.getOrderId());
        passbackParam.setUserId(launchPayDTO.getUserId());
        passbackParam.setGoodsItemId(launchPayDTO.getGoodsItemId());
        passbackParam.setOrderType(launchPayDTO.getTypeEnum().getType());
        alipayOrderCreateDTO.setPassbackParam(passbackParam);
        alipayOrderCreateDTO.setOrderType(launchPayDTO.getTypeEnum());
        alipayOrderCreateDTO.setTimeExpire(launchPayDTO.getExpireTime());
        String url = "";
        if(StringUtils.isEmpty(launchPayDTO.getPayMode()) || StringUtils.equals(launchPayDTO.getPayMode(), PayModeEnum.APP.getMode())){
            url = alipayTrader.createOrder(alipayOrderCreateDTO);
        }else if(StringUtils.equals(launchPayDTO.getPayMode(), PayModeEnum.QRCODE.getMode())){
            alipayOrderCreateDTO.setQrcodeWidth(launchPayDTO.getQrcodeWidth());
            url = alipayTrader.createOrderPagePay(alipayOrderCreateDTO);
        }
        return url;
    }

    @Override
    public PaymentCheckDTO paymentCheck(String userId, String queryId) {

        PaymentCheckDTO resp = new PaymentCheckDTO();
        PaymentPreCreateInfoDTO preCreateInfoCache = cacheUtil.getPreCreateInfoCache(queryId, PaymentPlatEnum.ALI_PAY.getCode());
        String orderId = queryId;
        if (null != preCreateInfoCache && null != preCreateInfoCache.getLaunchTimes()){
            queryId = queryId + "_" + preCreateInfoCache.getLaunchTimes();
        }
        resp.setStatus(PaymentCheckDTO.Status.UNKNOWN.getCode());
        AlipayTradeQueryResponse transaction = alipayTrader.getTransaction(queryId);
        log.info("[PaymentStatus] alipay. transaction is :{}, id is : {}", transaction, queryId);
        if (Objects.isNull(transaction) || !transaction.getCode().equals("10000")) {
            return resp;
        }
        if ("TRADE_SUCCESS".equals(transaction.getTradeStatus()) || "TRADE_FINISHED".equals(transaction.getTradeStatus())) {
            resp.setStatus(PaymentCheckDTO.Status.TRADE_SUCCESS.getCode());
            if (Objects.isNull(preCreateInfoCache)) {
                ShareOrder ShareOrder = orderManager.selByOrderId(orderId);
                if (Objects.nonNull(ShareOrder) && OrderStatusEnum.PAY_SUCCESSFULLY.getStatus().equals(ShareOrder.getStatus())) {
                    PaymentCheckDTO.OrderInfo orderInfo = new PaymentCheckDTO.OrderInfo();
                    orderInfo.setOrderId(ShareOrder.getOrderId());
                    orderInfo.setCreateTime(ShareOrder.getCreateTime().getTime());
                    resp.setOrderInfo(orderInfo);
                    return resp;
                }
                throw new CustomBusinessException(ErrorCodeUtils.CommonErrorCode.ERROR_SERVER_INTERNAL);
            }
            //TODO 异步处理。
            resendMsgService.sendAlipayTradeSuccessMsg(queryId, preCreateInfoCache, transaction);
            PaymentCheckDTO.OrderInfo orderInfo = new PaymentCheckDTO.OrderInfo();
            orderInfo.setOrderId(orderId);
            orderInfo.setCreateTime(preCreateInfoCache.getCreateTime());
            resp.setOrderInfo(orderInfo);
            return resp;
        }
        return resp;
    }

    @Override
    public boolean periodCheck(PaymentRegularCheckDTO paymentRegularCheckDTO, PaymentPreCreateInfoDTO preCreateInfoCache) {
        String queryId = paymentRegularCheckDTO.getQueryId();
        //订阅订单。
        if (paymentRegularCheckDTO.getIsSubscription() == 1) {
            AlipayUserAgreementQueryResponse agreement = alipayTrader.getAgreementByExternalAgreementNo(queryId);
            if (agreement.getCode().equals("10000") && agreement.getStatus().equals("NORMAL")) {
//                resendMsgService.sendAlipaySignSuccessMsg(queryId, agreement);
                return true;
            }
        }
        //普通订单
        else {
            AlipayTradeQueryResponse transaction = alipayTrader.getTransaction(queryId);
            log.info("transaction is :{}", transaction);
            if (Objects.isNull(transaction)) {
                return false;
            }
            if (AlipayEnum.TradeStatus.TRADE_CLOSED.name().equals(transaction.getTradeStatus())) {
                log.info("order is closed. param:{}", JSON.toJSONString(paymentRegularCheckDTO));
                return true;
            }
            if (transaction.getCode().equals("10000") && AlipayEnum.TradeStatus.TRADE_SUCCESS.name().equals(transaction.getTradeStatus())
                    || AlipayEnum.TradeStatus.TRADE_FINISHED.name().equals(transaction.getTradeStatus())) {
                resendMsgService.sendAlipayTradeSuccessMsg(queryId, preCreateInfoCache, transaction);
                return true;
            }
        }
        return false;
    }

    @Override
    public PaymentPlatEnum getTradePlat() {
        return PaymentPlatEnum.ALI_PAY;
    }

}
