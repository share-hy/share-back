package com.share.hy.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.share.hy.common.enums.PaymentPlatEnum;
import com.share.hy.dto.pay.OrderPreCreateResp;
import com.share.hy.dto.pay.PayCreateDTO;
import com.share.hy.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class IOrderServiceImpl implements IOrderService {

    @Autowired
    private

    @Override
    public OrderPreCreateResp preCreateOrder(PayCreateDTO payCreateDTO) {
        PaymentPlatEnum tradePlatEnum = PaymentPlatEnum.getByCode(param.getTradePlat());
        if (null == tradePlatEnum){
            throw new CustomBusinessException(ErrorCodeUtils.CommonErrorCode.ERROR_REQUEST_PARAMS);
        }
        OrderPreCreateResp resp = new OrderPreCreateResp();

        String orderId;
        int launchTimes = 1;
        String goodsItemId = param.getGoodsItemId();
        Byte type = param.getType();
        BigDecimal totalPrice = null;
        DateTime now = DateTime.now();
        Long expireTime = null;
        if (StrUtil.isNotBlank(param.getOrderId())) {
            orderId = param.getOrderId();
            PaymentOrder paymentOrder = orderManager.selByOrderId(orderId);
            if (null == paymentOrder){
                log.warn("not find the order, can not pay:{}",orderId);
                throw new CustomBusinessException(ErrorCodeUtils.PaymentErrorCode.ERROR_ORDER_NOT_EXIST);
            }
            if (null != paymentOrder.getStatus() && !paymentOrder.getStatus().equals(OrderStatusEnum.CREATED.getStatus())){
                log.warn("only wait payed order can be pay:{}", JSONObject.toJSONString(paymentOrder));
                throw new CustomBusinessException(ErrorCodeUtils.PaymentErrorCode.ERROR_ORDER_HAS_EXPIRED);
            }
            if (now.getTime() - paymentOrder.getCreateTime().getTime() > OrderConstant.DUE_OVERTIME_MILL_SECONDS){
                orderManager.updateOrderState(orderId,OrderStatusEnum.OVERTIME.getStatus());
                throw new CustomBusinessException(ErrorCodeUtils.PaymentErrorCode.ERROR_NOT_ALLOWED_TO_PAY);
            }
            launchTimes = paymentOrder.getLaunchTimes() + 1;
            paymentOrder.setLaunchTimes(launchTimes);
            orderManager.updateByOrderId(paymentOrder);
            expireTime = paymentOrder.getCreateTime().getTime() + OrderConstant.DUE_OVERTIME_MILL_SECONDS;
            List<PaymentOrderDetail> orderDetails = orderManager.queryOrderDetails(param.getOrderId());
            if (CollectionUtils.isNotEmpty(orderDetails)){
                PaymentOrderDetail orderDetail = orderDetails.get(0);
                goodsItemId = orderDetail.getSubjectId();
                type = orderDetail.getType();
                totalPrice = orderDetail.getAmount().multiply(new BigDecimal(orderDetail.getQuantity()));
            }
        } else {
            orderId = OrderUtil.generateOrderId();
        }
        String tradePlat = param.getTradePlat();
        OrderTypeEnum typeEnum = OrderTypeEnum.getOrderType(type);
        if (null == typeEnum){
            log.warn("order type wrong:{}",JSONObject.toJSONString(param));
            throw new CustomBusinessException(ErrorCodeUtils.CommonErrorCode.ERROR_REQUEST_PARAMS);
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
