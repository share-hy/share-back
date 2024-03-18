package com.share.hy.service.impl;

import com.share.hy.dto.admin.BenefitInfoDTO;
import com.share.hy.dto.admin.VipInfoDTO;
import com.share.hy.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IAdminServiceImpl implements IAdminService {

    @Autowired

    @Override
    public List<VipInfoDTO> vipQuery() {
        return null;
    }

    @Override
    public void vipConfig(List<VipInfoDTO> configs) {

    }

    @Override
    public List<BenefitInfoDTO> benefitQuery() {
        return null;
    }

    @Override
    public void benefitConfig(List<BenefitInfoDTO> benefitInfoDTOS) {

    }
}
