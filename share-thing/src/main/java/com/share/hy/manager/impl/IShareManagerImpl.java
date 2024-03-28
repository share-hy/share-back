package com.share.hy.manager.impl;

import cn.hutool.core.map.MapUtil;
import com.share.hy.domain.ShareBenefitRecord;
import com.share.hy.manager.IShareManager;
import com.share.hy.mapper.ShareBenefitRecordMapper;
import com.share.hy.mapper.ShareUserRelationMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class IShareManagerImpl implements IShareManager {

    @Autowired
    private ShareBenefitRecordMapper benefitRecordMapper;

    @Autowired
    private ShareUserRelationMapper userRelationMapper;

    @Override
    public Map<Byte, BigDecimal> countBenefitGroupByLevel(String userId) {
        List<Map<String, Object>> groupByLevel = benefitRecordMapper.countBenefitGroupByLevel(userId);
        if (CollectionUtils.isEmpty(groupByLevel)){
            return Collections.emptyMap();
        }
        Map<Byte, BigDecimal> result = new HashMap<>(2,1.01f);
        groupByLevel.forEach(map ->{
            byte level = Byte.parseByte(map.get("level").toString());
            BigDecimal amount = new BigDecimal(map.get("amount").toString());
            result.put(level,amount);
        });
        return result;
    }

    @Override
    public Map<Byte, Integer> countSubordinateGroupByLevel(String userId) {
        List<Map<String, Object>> groupByLevel = userRelationMapper.countSubordinateGroupByLevel(userId);
        if (CollectionUtils.isEmpty(groupByLevel)){
            return Collections.emptyMap();
        }
        Map<Byte, Integer> result = new HashMap<>(2,1.01f);
        groupByLevel.forEach(map ->{
            byte level = Byte.parseByte(map.get("level").toString());
            int amount = Integer.parseInt(map.get("amount").toString());
            result.put(level,amount);
        });
        return result;
    }

    @Override
    public List<ShareBenefitRecord> queryByLevelAndUserId(String userId, Byte level,Integer pageSize,Integer pageNum) {
        Example example = new Example(ShareBenefitRecord.class);
        example.createCriteria().andEqualTo("userId",userId)
                .andEqualTo("level",level);
        RowBounds rowBounds = new RowBounds((pageNum - 1) * pageSize, pageSize);
        return benefitRecordMapper.selectByExampleAndRowBounds(example,rowBounds);
    }

    @Override
    public int countByUserIdAndLevel(String userId, Byte level) {
        Example example = new Example(ShareBenefitRecord.class);
        example.createCriteria().andEqualTo("userId",userId)
                .andEqualTo("level",level);
        return benefitRecordMapper.selectCountByExample(example);
    }
}
