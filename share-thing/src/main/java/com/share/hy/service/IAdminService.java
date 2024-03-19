package com.share.hy.service;

import com.share.hy.dto.admin.BenefitInfoDTO;
import com.share.hy.dto.admin.VipInfoDTO;

import java.util.List;

public interface IAdminService {

    List<VipInfoDTO> vipQuery();

    void vipConfig(List<VipInfoDTO> configs,String operateId);

    List<BenefitInfoDTO> benefitQuery();

    void benefitConfig(List<BenefitInfoDTO> benefitInfoDTOS,String operateId);



}
