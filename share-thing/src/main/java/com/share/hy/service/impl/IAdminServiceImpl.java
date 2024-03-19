package com.share.hy.service.impl;

import com.share.hy.domain.ShareBenefitConfig;
import com.share.hy.domain.ShareVipConfig;
import com.share.hy.dto.admin.BenefitInfoDTO;
import com.share.hy.dto.admin.VipInfoDTO;
import com.share.hy.manager.IAdminManager;
import com.share.hy.service.IAdminService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IAdminServiceImpl implements IAdminService {

    @Autowired
    private IAdminManager adminManager;

    @Override
    public List<VipInfoDTO> vipQuery() {
        List<ShareVipConfig> configs = adminManager.vipQuery();
        return CollectionUtils.isEmpty(configs) ? Collections.emptyList() :
                configs.stream().map(VipInfoDTO::new).collect(Collectors.toList());
    }

    @Override
    public void vipConfig(List<VipInfoDTO> configs,String operateId) {
        adminManager.vipConfig(configs.stream().map(n->new ShareVipConfig(n,operateId)).collect(Collectors.toList()));
    }

    @Override
    public List<BenefitInfoDTO> benefitQuery() {
        List<ShareBenefitConfig> configs = adminManager.benefitQuery();
        return CollectionUtils.isEmpty(configs) ? Collections.emptyList() :
                configs.stream().map(BenefitInfoDTO::new).collect(Collectors.toList());
    }

    @Override
    public void benefitConfig(List<BenefitInfoDTO> configs,String operateId) {
        adminManager.benefitConfig(configs.stream().map(n->new ShareBenefitConfig(n,operateId))
                .collect(Collectors.toList()));
    }
}
