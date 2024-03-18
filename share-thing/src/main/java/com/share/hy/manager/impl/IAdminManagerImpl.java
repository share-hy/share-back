package com.share.hy.manager.impl;

import com.share.hy.domain.ShareBenefitConfig;
import com.share.hy.domain.ShareVipConfig;
import com.share.hy.manager.IAdminManager;
import com.share.hy.mapper.ShareBenefitConfigMapper;
import com.share.hy.mapper.ShareVipConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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

    }

    @Override
    public List<ShareBenefitConfig> benefitQuery() {
        return null;
    }

    @Override
    public void benefitConfig(List<ShareBenefitConfig> benefitInfoDTOS) {

    }
}
