package com.share.hy.manager;

import com.share.hy.domain.ShareBenefitConfig;
import com.share.hy.domain.ShareVipConfig;

import java.util.List;

public interface IAdminManager {

    List<ShareVipConfig> vipQuery();

    void vipConfig(List<ShareVipConfig> configs);

    List<ShareBenefitConfig> benefitQuery();

    void benefitConfig(List<ShareBenefitConfig> benefitInfoDTOS);
}
