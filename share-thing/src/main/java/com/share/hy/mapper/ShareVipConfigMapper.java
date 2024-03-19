package com.share.hy.mapper;

import com.share.hy.common.BaseMapper;
import com.share.hy.domain.ShareVipConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface ShareVipConfigMapper extends BaseMapper<ShareVipConfig> {

    void updateOrAdd(@Param("list") List<ShareVipConfig> configs);
}