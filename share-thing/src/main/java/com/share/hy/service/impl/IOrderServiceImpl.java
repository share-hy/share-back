package com.share.hy.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.domain.OrderGoodsDTO;
import com.alipay.api.domain.PaymentGoods;
import com.share.hy.common.CustomBusinessException;
import com.share.hy.common.constants.OrderConstant;
import com.share.hy.common.enums.*;
import com.share.hy.domain.ShareGoodsItem;
import com.share.hy.domain.ShareOrder;
import com.share.hy.domain.ShareUserTradeRecord;
import com.share.hy.dto.pay.*;
import com.share.hy.manager.GoodsManager;
import com.share.hy.manager.IOrderManager;
import com.share.hy.manager.IOrderTradeRecordManager;
import com.share.hy.service.IOrderService;
import com.share.hy.service.pay.PaymentFuncHolder;
import com.share.hy.utils.CacheUtil;
import com.share.hy.utils.OrderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.websocket.SendResult;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IOrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderManager orderManager;

    @Autowired
    private CacheUtil cacheUtil;

    @Autowired
    private GoodsManager goodsItemManager;

    @Autowired
    private IOrderTradeRecordManager orderTradeRecordManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

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
        orderId = OrderUtil.generateOrderId();
        String tradePlat = payCreateDTO.getTradePlat();
        OrderTypeEnum typeEnum = OrderTypeEnum.getOrderType(type);
        if (null == typeEnum){
            log.warn("order type wrong:{}",JSONObject.toJSONString(payCreateDTO));
            throw new CustomBusinessException(ErrorCodeEnum.ERROR_PARAM_WRONG);
        }

        ShareGoodsItem goodsItem = goodsItemManager.queryByGoodsItemId(goodsItemId);
        if (null == goodsItem){
            log.warn("goodsItem not exist:{}",JSONObject.toJSONString(payCreateDTO));
            throw new CustomBusinessException(ErrorCodeEnum.ERROR_PARAM_WRONG);
        }
        resp.setQueryId(orderId);
        resp.setOrderId(orderId);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String expireTimeStr = format.format(new Date(OrderConstant.DUE_OVERTIME_MILL_SECONDS));

        LaunchPayDTO launchPayDTO = new LaunchPayDTO(orderId,payCreateDTO,
                orderId, typeEnum,expireTimeStr);

        String url = PaymentFuncHolder.getPaymentService(tradePlatEnum).launchPay(launchPayDTO);

        resp.setTarget(url);

        AlipayPayload alipayPayload = new AlipayPayload();
        alipayPayload.setOrder(new AlipayPayload.Order(orderId));
        String alipayPayloadStr = JSON.toJSONString(alipayPayload);

        PaymentPreCreateInfoDTO paymentPrecreateInfoDTO = new PaymentPreCreateInfoDTO();
        paymentPrecreateInfoDTO.setTradePlat(tradePlat);
        paymentPrecreateInfoDTO.setPayload(alipayPayloadStr);
        paymentPrecreateInfoDTO.setUserId(payCreateDTO.getUserId());
        paymentPrecreateInfoDTO.setGoodsItemId(goodsItemId);
        paymentPrecreateInfoDTO.setOrderType(typeEnum.getType());
        paymentPrecreateInfoDTO.setCreateTime(System.currentTimeMillis());
        paymentPrecreateInfoDTO.setLaunchTimes(launchTimes);

        cacheUtil.setPreCreateInfoCache(resp.getQueryId(), tradePlat, JSON.toJSONString(paymentPrecreateInfoDTO), Duration.of(1, ChronoUnit.DAYS));

        //定期检查订单是否完成。
        periodicCheck(new PaymentRegularCheckDTO(tradePlat, resp.getQueryId(), 1));
        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int orderCompleted(String orderId, Date payTime) {
        ShareOrder order = orderManager.selByOrderId(orderId);
        if (Objects.isNull(order)) {
            throw new CustomBusinessException(ErrorCodeEnum.ERROR_ORDER_NOT_EXIST);
        }
        //插入交易单.
        ShareUserTradeRecord orderTradeRecord = new ShareUserTradeRecord();
        orderTradeRecord.setOrderId(order.getOrderId());
        orderTradeRecord.setTradeId(order.getTradeId());
        orderTradeRecord.setTradePlat(order.getTradePlat());
        orderTradeRecord.setAmount(order.getPaymentAmount());
        orderTradeRecord.setCurrency(order.getCurrency());
        orderTradeRecord.setType(TradeTypeEnum.INCOME.getType());
        orderTradeRecord.setCreateTime(payTime);
        orderTradeRecordManager.insert(orderTradeRecord);
        ShareOrder toUpdateOrder = new ShareOrder();
        toUpdateOrder.setOrderId(orderId);
        toUpdateOrder.setStatus(OrderStatusEnum.PAY_SUCCESSFULLY.getStatus());
        toUpdateOrder.setPayTime(payTime);
        return orderManager.updateByOrderId(toUpdateOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderCompletedDirectly(ShareOrder toInsertOrder,boolean update) {
        //非支付成功状态的订单不允许使用该api
        if (!OrderStatusEnum.PAY_SUCCESSFULLY.getStatus().equals(toInsertOrder.getStatus())) {
            throw new RuntimeException("order status must be paid.");
        }
        //插入交易单.
        ShareUserTradeRecord orderTradeRecord = new ShareUserTradeRecord();
        orderTradeRecord.setOrderId(toInsertOrder.getOrderId());
        orderTradeRecord.setTradeId(toInsertOrder.getTradeId());
        orderTradeRecord.setTradePlat(toInsertOrder.getTradePlat());
        orderTradeRecord.setAmount(toInsertOrder.getPaymentAmount());
        orderTradeRecord.setCurrency(toInsertOrder.getCurrency());
        orderTradeRecord.setType(TradeTypeEnum.INCOME.getType());
        orderTradeRecord.setCreateTime(toInsertOrder.getCreateTime());
        if (update){
            orderManager.updateByOrderId(toInsertOrder);
        }
        else{
            orderManager.insert(toInsertOrder);
        }
        orderTradeRecordManager.insert(orderTradeRecord);
        OrderFinishMsgDTO finishMsgDTO =new OrderFinishMsgDTO(toInsertOrder.getOrderId(), toInsertOrder.getUserId(),toInsertOrder.getGoodsItemId());
        String sendStr = JSONObject.toJSONString(finishMsgDTO);
//        cacheUtil.clearPreCreateOrderCache(toInsertOrder.getUserId(), orderDetail.getSubjectId());
        SendResult sendResult = rocketMqProducer.syncSendForResult(RocketMqTopicConstant.TOPIC_PAY_RESULT_EVENT, null, sendStr);
        log.info("order finish,send msg:{},result:{}",sendStr,JSONObject.toJSONString(sendResult));

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int closeOrder(String orderId) {
        ShareOrder order = orderManager.selByOrderId(orderId);
        if (Objects.isNull(order)) {
            throw new CustomBusinessException(ErrorCodeEnum.ERROR_ORDER_NOT_EXIST);
        }
        //如果不是待支付的订单，不允许关闭。
        if (!order.getStatus().equals(OrderStatusEnum.CREATED.getStatus())) {
            log.warn("[Order] close. the order can't be cancelled.currentStatus:{}", order.getStatus());
            return 0;
        }
        return orderManager.updateOrderState(order.getOrderId(),OrderStatusEnum.CLOSED.getStatus());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int closeOrder(ShareOrder order) {
        if (Objects.isNull(order)) {
            throw new CustomBusinessException(ErrorCodeEnum.ERROR_ORDER_NOT_EXIST);
        }
        //如果不是待支付的订单，不允许关闭。
        if (!order.getStatus().equals(OrderStatusEnum.CREATED.getStatus())) {
            log.warn("[Order] close. the order can't be cancelled.currentStatus:{}", order.getStatus());
            return 0;
        }
        return orderManager.updateOrderState(order.getOrderId(),OrderStatusEnum.CLOSED.getStatus());
    }

    @Override
    public void refund(String orderId, BigDecimal refundedAmount, String currency, Date refundTime) {
        ShareOrder ShareOrder = orderManager.selByOrderId(orderId);
        if (Objects.isNull(ShareOrder)) {
            log.warn("[PaymentCB] refund. order not exist. orderId:{}", orderId);
            throw new CustomBusinessException(ErrorCodeEnum.ERROR_ORDER_NOT_EXIST);
        }
        this.refund(ShareOrder, refundedAmount, currency, refundTime);
    }


    @Override
    public void refund(ShareOrder order, BigDecimal refundedAmount, String currency, Date refundTime) {
        if (Objects.isNull(order)) {
            log.warn("[PaymentCB] refund. order not exist.");
            throw new CustomBusinessException(ErrorCodeEnum.ERROR_ORDER_NOT_EXIST);
        }

        BigDecimal absRefundedAmount = refundedAmount.abs();

        //非支付完成的订单不允许退款。
        if (!OrderStatusEnum.PAY_SUCCESSFULLY.getStatus().equals(order.getStatus())) {
            log.error("[Order] refund. Order status isn't PAY_SUCCESSFULLY. status:{}",order.getStatus());
            throw new CustomBusinessException(ErrorCodeEnum.ERROR_ORDER_CANNOT_REFUND_STATUS_INCORRECT);
        }

        /*
        1.更新订单主表(退款金额，退款时间，退款币种，订单状态)
        2.如果是订阅订单，取消订阅。（调用第三方交易平台，先取消订阅关系，然后更新订阅状态表，删除订阅主体和订阅订单的关系。）
        3.插入扣款流水。
         */
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            ShareOrder toUpdateOrder = new ShareOrder();
            toUpdateOrder.setRefundAmount(absRefundedAmount);
            toUpdateOrder.setRefundTime(refundTime);
            toUpdateOrder.setOrderId(order.getOrderId());
            toUpdateOrder.setRefundCurrency(currency);
            toUpdateOrder.setStatus(OrderStatusEnum.REFUNDED.getStatus());
            orderManager.updateByOrderId(toUpdateOrder);

            ShareUserTradeRecord ShareUserTradeRecord = new ShareUserTradeRecord();
            ShareUserTradeRecord.setOrderId(order.getOrderId());
            ShareUserTradeRecord.setTradeId(order.getTradeId());
            ShareUserTradeRecord.setTradePlat(order.getTradePlat());
            ShareUserTradeRecord.setAmount(absRefundedAmount.negate());
            ShareUserTradeRecord.setCurrency(currency);
            ShareUserTradeRecord.setType(TradeTypeEnum.EXPENDITURES.getType());
            ShareUserTradeRecord.setCreateTime(refundTime);
            orderTradeRecordManager.insert(ShareUserTradeRecord);
        });
    }

    @Override
    public void postCreationOrder(OrderCreateParamDTO param) {
        String orderId = param.getOrderId();
        ShareOrder toInsertOrder;
        boolean update;
        if (update = StrUtil.isNotBlank(orderId)) {
            toInsertOrder = orderManager.selByOrderId(orderId);
        }
        else{
            orderId = OrderUtil.generateOrderId();
            toInsertOrder = new ShareOrder();
        }

        String tradeNo = param.getTradeId();
        String internalTradeId = OrderUtil.getTradeId(param.getTradePlat(), tradeNo);
        String userId = param.getUserId();
        String goodsItemId = param.getGoodsItemId();

        Date createTime = new Date(param.getCreateTime());
        Date payTime = new Date(param.getPaymentTime());


        toInsertOrder.setOrderId(orderId);
        toInsertOrder.setStatus(OrderStatusEnum.PAY_SUCCESSFULLY.getStatus());//!!!这块是直接支付完成状态。
        toInsertOrder.setTradePlat(param.getTradePlat());

        toInsertOrder.setPaymentAmount(param.getTotalAmount());
        toInsertOrder.setPayTime(payTime);
        toInsertOrder.setUserId(userId);
        toInsertOrder.setTradeId(internalTradeId);
        toInsertOrder.setCurrency(param.getCurrency());
        toInsertOrder.setCreateTime(payTime);
        toInsertOrder.setLatestUpdateTime(payTime);
        toInsertOrder.setCreateTime(createTime);
        orderCompletedDirectly(toInsertOrder,update);
    }

    @Override
    public void periodicCheck(PaymentRegularCheckDTO paymentRegularCheckDTO) {
        //最大6次。
        Integer delayLevel = delayLevelMap.get(paymentRegularCheckDTO.getRetryTimes());
        if (Objects.isNull(delayLevel)) {
            log.info("periodicCheck. msg not send. param:{}", JSON.toJSONString(paymentRegularCheckDTO));
            return;
        }
        SendResult sendResult = rocketMqProducer.syncSendDelayForResult(RocketMqTopicConstant.TOPIC_PAY_ORDER_EVENT, "regularCheck", paymentRegularCheckDTO, delayLevel);
        log.info("periodicCheck res:{}",sendResult);
    }

    @Override
    public List<OrderInfoDTO> queryOrder(OrderQueryDTO queryDTO) {
        List<ShareOrder> shareOrders = orderManager.selByUserIdAndStatus(queryDTO.getUserId(), queryDTO.getStatus());
        if (CollectionUtils.isEmpty(shareOrders)){
            return Collections.emptyList();
        }
        List<String> orderIds = shareOrders.stream().map(ShareOrder::getOrderId).collect(Collectors.toList());
        List<String> goodsItemIds = shareOrders.stream().map(ShareOrder::getGoodsItemId).collect(Collectors.toList());
        List<ShareGoodsItem> goodsItems = goodsItemManager.queryByGoodsItemIds(goodsItemIds);
        if (CollectionUtils.isEmpty(goodsItems)){
            log.warn("not find goodsItem info:{}",JSONObject.toJSONString(goodsItemIds));
            throw new CustomBusinessException(ErrorCodeEnum.ERROR_PARAM_WRONG);
        }
        Map<String, ShareGoodsItem> goodsItemMap = goodsItems.stream().collect(Collectors.toMap(ShareGoodsItem::getGoodsItemId, k -> k));
        List<ShareUserTradeRecord> tradeRecords = orderTradeRecordManager.getByOrderIds(orderIds);
        if (CollectionUtils.isEmpty(tradeRecords)){
            log.warn("not find order trade info:{}",JSONObject.toJSONString(orderIds));
            throw new CustomBusinessException(ErrorCodeEnum.ERROR_PARAM_WRONG);
        }
        Map<String, List<ShareUserTradeRecord>> orderTradeMaps = tradeRecords.stream().collect(Collectors.groupingBy(ShareUserTradeRecord::getOrderId));
        List<String> needUpdateOvertime = new LinkedList<>();
        List<OrderInfoDTO> data = shareOrders.stream()
                .map(n -> new OrderInfoDTO(n, goodsItemMap.get(n.getGoodsItemId()),,orderTradeMaps.get(n.getOrderId())))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(needUpdateOvertime)){
            orderManager.updateOvertimeBatch(needUpdateOvertime);
        }
        return new PageCommon<>(count,data);


    }
}
