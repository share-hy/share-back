package com.share.hy.mapper;

import com.share.hy.common.BaseMapper;
import com.share.hy.domain.ShareBenefitConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface ShareBenefitConfigMapper extends BaseMapper<ShareBenefitConfig> {
    void updateOrAdd(@Param("list") List<ShareBenefitConfig> benefitInfoDTOS);
}