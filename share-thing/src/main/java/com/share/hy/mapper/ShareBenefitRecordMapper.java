package com.share.hy.mapper;

import com.share.hy.common.BaseMapper;
import com.share.hy.domain.ShareBenefitRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface ShareBenefitRecordMapper extends BaseMapper<ShareBenefitRecord> {
    List<Map<String, Object>> countBenefitGroupByLevel(@Param("userId") String userId);
}