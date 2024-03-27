package com.share.hy.mapper;

import com.share.hy.common.BaseMapper;
import com.share.hy.domain.ShareUserRelation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ShareUserRelationMapper extends BaseMapper<ShareUserRelation> {
    List<Map<String, Object>> countSubordinateGroupByLevel(String userId);
}