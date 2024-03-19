package com.share.hy.manager.impl;

import com.alibaba.fastjson.JSONObject;
import com.share.hy.domain.ShareBenefitConfig;
import com.share.hy.domain.ShareVipConfig;
import com.share.hy.manager.IAdminManager;
import com.share.hy.mapper.ShareBenefitConfigMapper;
import com.share.hy.mapper.ShareVipConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class IAdminManagerImpl implements IAdminManager {

    @Autowired
    private ShareVipConfigMapper shareVipConfigMapper;

    @Autowired
    private ShareBenefitConfigMapper shareBenefitConfigMapper;

    @Override
    public List<ShareVipConfig> vipQuery() {
        return shareVipConfigMapper.selectAll();
    }

    @Override
    public void vipConfig(List<ShareVipConfig> configs) {
        shareVipConfigMapper.updateOrAdd(configs);
        log.info("vip config success:{}", JSONObject.toJSONString(configs));
    }

    @Override
    public List<ShareBenefitConfig> benefitQuery() {
        return shareBenefitConfigMapper.selectAll();
    }

    @Override
    public void benefitConfig(List<ShareBenefitConfig> benefitInfoDTOS) {
        shareBenefitConfigMapper.updateOrAdd(benefitInfoDTOS);
        log.info("share benefit config success:{}",JSONObject.toJSONString(benefitInfoDTOS));
    }
}
