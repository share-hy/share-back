package com.share.hy.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.share.hy.common.constants.RedisKeyConstant;
import com.share.hy.domain.ShareOrder;
import com.share.hy.manager.IOrderManager;
import com.share.hy.mapper.ShareOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OrderManagerImpl implements IOrderManager {

    @Autowired
    private ShareOrderMapper shareOrderMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public int insert(ShareOrder order) {
        return shareOrderMapper.insertSelective(order);
    }

    @Override
    public int updateOrderState(String orderId, Byte orderStatus) {
        ShareOrder ShareOrder = new ShareOrder();
        ShareOrder.setStatus(orderStatus);
        Example example = new Example(ShareOrder.class);
        example.createCriteria().andEqualTo("orderId", orderId);
        int i = shareOrderMapper.updateByExampleSelective(ShareOrder, example);
        this.clearCacheShareOrder(orderId);
        return i;
    }


    @Override
    public ShareOrder selByOrderId(String orderId) {
        ShareOrder ShareOrder = this.getCacheShareOrder(orderId);
        if (Objects.nonNull(ShareOrder)) {
            return ShareOrder;
        }
        Example example = new Example(ShareOrder.class);
        example.createCriteria().andEqualTo("orderId", orderId);
        ShareOrder order = shareOrderMapper.selectOneByExample(example);
        this.setCacheShareOrder(order);
        return order;
    }

    @Override
    public List<ShareOrder> selByOrderId(List<String> orderIds) {
        if (CollUtil.isEmpty(orderIds)) {
            return Collections.emptyList();
        }

        List<ShareOrder> resList = new ArrayList<>();
        List<String> toRebuildCacheOrderIds = new ArrayList<>();

        List<String> keyList = orderIds.stream().map(this::getOrderRdsKey).collect(Collectors.toList());
        List<String> strings = stringRedisTemplate.opsForValue().multiGet(keyList);
        if (CollUtil.isEmpty(strings)) {
            toRebuildCacheOrderIds.addAll(orderIds);
        } else {
            for (int i = 0; i < orderIds.size(); i++) {
                if (strings.size()  > i && StrUtil.isBlank(strings.get(i))) {
                    toRebuildCacheOrderIds.add(orderIds.get(i));
                } else {
                    resList.add(JSON.parseObject(strings.get(i), ShareOrder.class));
                }
            }
        }

        if (CollUtil.isEmpty(toRebuildCacheOrderIds)) {
            return resList;
        }
        Example example = new Example(ShareOrder.class);
        example.createCriteria().andIn("orderId", toRebuildCacheOrderIds);
        List<ShareOrder> ShareOrders = shareOrderMapper.selectByExample(example);
        resList.addAll(ShareOrders);
        for (ShareOrder ShareOrder : ShareOrders) {
            this.setCacheShareOrder(ShareOrder);
        }
        return resList;
    }

    @Override
    public ShareOrder selByTradeId(String tradeId) {
        Example example = new Example(ShareOrder.class);
        example.createCriteria().andEqualTo("tradeId", tradeId);
        return shareOrderMapper.selectOneByExample(example);
    }

    @Override
    public int updateByOrderId(ShareOrder order) {
        Example example = new Example(ShareOrder.class);
        example.createCriteria().andEqualTo("orderId", order.getOrderId());
        int i = shareOrderMapper.updateByExampleSelective(order, example);
        this.clearCacheShareOrder(order.getOrderId());
        return i;
    }

    @Override
    public ShareOrder selByUserIdAndOrderId(String userId, String orderId) {
        Example example = new Example(ShareOrder.class);
        example.createCriteria().andEqualTo("orderId", orderId)
                .andEqualTo("userId", userId);
        return shareOrderMapper.selectOneByExample(example);
    }

    @Override
    public List<ShareOrder> selByUserIdAndStatus(String userId, List<Byte> status) {
        if (StringUtils.isBlank(userId)){
            return Collections.emptyList();
        }
        Example example = new Example(ShareOrder.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("userId", userId);
        if (CollectionUtils.isNotEmpty(status)){
            criteria.andIn("status",status);
        }
        return shareOrderMapper.selectByExample(example);
    }

    private void clearCacheShareOrder(String orderId) {
        stringRedisTemplate.delete(this.getOrderRdsKey(orderId));
    }

    private void setCacheShareOrder(ShareOrder ShareOrder) {
        if (Objects.isNull(ShareOrder)) {
            return;
        }
        stringRedisTemplate.opsForValue().set(this.getOrderRdsKey(ShareOrder.getOrderId()),
                JSON.toJSONString(ShareOrder), Duration.of(1, ChronoUnit.DAYS));
    }

    private ShareOrder getCacheShareOrder(String orderId) {
        String s = stringRedisTemplate.opsForValue().get(this.getOrderRdsKey(orderId));
        if (StrUtil.isNotBlank(s)) {
            return JSON.parseObject(s, ShareOrder.class);
        }
        return null;
    }

    private String getOrderRdsKey(String orderId) {
        return RedisKeyConstant.PAY_REDIS_KEY + "order:" + orderId;
    }
}
