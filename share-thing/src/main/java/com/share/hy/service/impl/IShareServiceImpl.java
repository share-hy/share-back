package com.share.hy.service.impl;

import cn.hutool.core.map.MapUtil;
import com.share.hy.domain.ShareBenefitRecord;
import com.share.hy.dto.console.EarningsOverviewDTO;
import com.share.hy.dto.console.ShareBenefitDTO;
import com.share.hy.dto.console.SubordinateOverviewDTO;
import com.share.hy.manager.IShareManager;
import com.share.hy.service.IShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IShareServiceImpl implements IShareService {

    @Autowired
    private Environment env;

    @Autowired
    private IShareManager shareManager;

    @Override
    public Map<String, String> inviteLink(String userId) {
        String link = env.getProperty("invite.link", String.class, "https://yhy-test.share.com?s=");
        return Collections.singletonMap("url",link + userId);
    }

    @Override
    public EarningsOverviewDTO earningsOverview(String userId) {
        Map<Byte, BigDecimal> levelIncomeMap = shareManager.countBenefitGroupByLevel(userId);
        if (MapUtil.isEmpty(levelIncomeMap)){
            return new EarningsOverviewDTO(new BigDecimal(0),Collections.emptyList());
        }
        EarningsOverviewDTO overviewDTO = new EarningsOverviewDTO();
        double sum = levelIncomeMap.values().stream().mapToDouble(BigDecimal::doubleValue).sum();
        overviewDTO.setTotalAmount(new BigDecimal(sum));
        List<EarningsOverviewDTO.IncomeGroupInfo> group = new LinkedList<>();
        levelIncomeMap.forEach((k,v)->{
            group.add(new EarningsOverviewDTO.IncomeGroupInfo(v,k));
        });
        overviewDTO.setGroup(group);
        return overviewDTO;
    }

    @Override
    public SubordinateOverviewDTO subordinateOverview(String userId) {
        Map<Byte, Integer> byteIntegerMap = shareManager.countSubordinateGroupByLevel(userId);
        if (MapUtil.isEmpty(byteIntegerMap)){
            return new SubordinateOverviewDTO(0,Collections.emptyList());
        }
        SubordinateOverviewDTO overviewDTO = new SubordinateOverviewDTO();
        List<SubordinateOverviewDTO.PersonalGroupInfo> group = new LinkedList<>();
        overviewDTO.setTotal(byteIntegerMap.values().stream().mapToInt(k -> k).sum());
        byteIntegerMap.forEach((k,v)->{
            group.add(new SubordinateOverviewDTO.PersonalGroupInfo(v,k));
        });
        overviewDTO.setGroup(group);
        return overviewDTO;
    }

    @Override
    public Map<String,Object> incomeDetail(String userId, Byte level,Integer pageSize,Integer pageNum) {
        int count = shareManager.countByUserIdAndLevel(userId, level);
        Map<String, Object> result = new HashMap<>(2,1.01f);
        if (count == 0){
            result.put("count",0);
            result.put("data",Collections.emptyList());
            return result;
        }
        List<ShareBenefitRecord> benefitRecords = shareManager.queryByLevelAndUserId(userId, level, pageSize, pageNum);
        result.put("data",benefitRecords.stream().map(ShareBenefitDTO::new).collect(Collectors.toList()));
        result.put("count",count);
        return result;
    }
}
