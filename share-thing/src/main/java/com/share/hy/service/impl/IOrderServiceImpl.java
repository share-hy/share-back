package com.share.hy.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.share.hy.common.CustomBusinessException;
import com.share.hy.common.enums.ErrorCodeEnum;
import com.share.hy.common.enums.OrderTypeEnum;
import com.share.hy.common.enums.PaymentPlatEnum;
import com.share.hy.dto.pay.OrderPreCreateResp;
import com.share.hy.dto.pay.PayCreateDTO;
import com.share.hy.service.IOrderService;
import com.share.hy.utils.OrderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class IOrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderManager orderManager;

    @Override
    public OrderPreCreateResp preCreateOrder(PayCreateDTO payCreateDTO) {
        PaymentPlatEnum tradePlatEnum = PaymentPlatEnum.getByCode(payCreateDTO.getTradePlat());
        if (null == tradePlatEnum){
            throw new CustomBusinessException(ErrorCodeEnum.ERROR_SERVER_ERROR);
        }
        OrderPreCreateResp resp = new OrderPreCreateResp();

        String orderId;
        int launchTimes = 1;
        String goodsItemId = payCreateDTO.getGoodsItemId();
        Byte type = payCreateDTO.getType();
        BigDecimal totalPrice = null;
        DateTime now = DateTime.now();
        Long expireTime = null;
        orderId = OrderUtil.generateOrderId();
        String tradePlat = payCreateDTO.getTradePlat();
        OrderTypeEnum typeEnum = OrderTypeEnum.getOrderType(type);
        if (null == typeEnum){
            log.warn("order type wrong:{}",JSONObject.toJSONString(payCreateDTO));
            throw new CustomBusinessException(ErrorCodeEnum.ERROR_PARAM_WRONG);
        }

        PaymentGoodsItem goodsItem = goodsItemManager.getGoodsItem(goodsItemId);
        if (null == goodsItem){
            log.warn("goodsItem not exist:{}",JSONObject.toJSONString(param));
            throw new CustomBusinessException(ErrorCodeUtils.CommonErrorCode.ERROR_REQUEST_PARAMS);
        }
        if (null == totalPrice){
            totalPrice = goodsItem.getRawPrice();
        }

        resp.setQueryId(orderId);
        resp.setOrderId(orderId);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String expireTimeStr = format.format(new Date(expireTime == null ? now.getTime() + OrderConstant.DUE_OVERTIME_MILL_SECONDS : expireTime));

        LaunchPayDTO launchPayDTO = new LaunchPayDTO(orderId + "_" + launchTimes, totalPrice , goodsItem.getGoodsItemId(),
                orderId, param.getUserId(), typeEnum, param.getPayMode(), param.getQrcodeWidth(),expireTimeStr);

        String url = PaymentFuncHolder.getPaymentService(tradePlatEnum).launchPay(launchPayDTO);

        JSONObject respPayload = new JSONObject();
        respPayload.put("url", url);
        resp.setPayloadToClient(respPayload);

        AlipayPayload alipayPayload = new AlipayPayload();
        alipayPayload.setOrder(new AlipayPayload.Order(orderId));
        String alipayPayloadStr = JSON.toJSONString(alipayPayload);

        PaymentPreCreateInfoDTO paymentPrecreateInfoDTO = new PaymentPreCreateInfoDTO();
        paymentPrecreateInfoDTO.setTradePlat(tradePlat);
        paymentPrecreateInfoDTO.setPayload(alipayPayloadStr);
        paymentPrecreateInfoDTO.setUserId(param.getUserId());
        paymentPrecreateInfoDTO.setGoodsItemId(goodsItemId);
        paymentPrecreateInfoDTO.setSubscription(goodsItem.getSubscribed());
        paymentPrecreateInfoDTO.setOrderType(typeEnum.getType());
        paymentPrecreateInfoDTO.setCreateTime(System.currentTimeMillis());
        paymentPrecreateInfoDTO.setLaunchTimes(launchTimes);

        cacheUtil.setPreCreateInfoCache(resp.getQueryId(), tradePlat, JSON.toJSONString(paymentPrecreateInfoDTO), Duration.of(1, ChronoUnit.DAYS));

        //定期检查订单是否完成。
        periodicCheck(new PaymentRegularCheckDTO(tradePlat, orderId + "_" + launchTimes, 0, 1));
    }
}
