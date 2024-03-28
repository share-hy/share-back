package com.share.hy.manager.impl;

import com.alibaba.fastjson.JSON;
import com.share.hy.domain.ShareUserTradeRecord;
import com.share.hy.manager.IUserTradeRecordManager;
import com.share.hy.mapper.ShareUserTradeRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class UserTradeRecordManagerImpl implements IUserTradeRecordManager {


    @Autowired
    private ShareUserTradeRecordMapper orderTradeRecordMapper;


    @Override
    public int insert(ShareUserTradeRecord orderTradeRecord) {
        if (Objects.isNull(orderTradeRecord) || BigDecimal.ZERO.compareTo(orderTradeRecord.getAmount()) == 0) {
            log.warn("records is null or amount equals zero. abort action. param:{}", JSON.toJSONString(orderTradeRecord));
            return 0;
        }
        return orderTradeRecordMapper.insertSelective(orderTradeRecord);
    }

    @Override
    public List<ShareUserTradeRecord> getByOrderIds(List<String> orderIds) {
        if (CollectionUtils.isEmpty(orderIds)){
            return Collections.emptyList();
        }
        Example example = new Example(ShareUserTradeRecord.class);
        example.createCriteria().andIn("orderId",orderIds);
        return orderTradeRecordMapper.selectByExample(example);
    }

    @Override
    public List<ShareUserTradeRecord> queryBalanceByUserId(String userId) {
        if (StringUtils.isBlank(userId)){
            return Collections.emptyList();
        }
        Example example = new Example(ShareUserTradeRecord.class);
        example.createCriteria().andEqualTo("userId",userId)
                .andEqualTo("from",1);
        return orderTradeRecordMapper.selectByExample(example);
    }
}