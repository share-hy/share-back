package com.share.hy.manager;

import com.share.hy.domain.ShareBenefitRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IShareManager {

    Map<Byte, BigDecimal> countBenefitGroupByLevel(String userId);

    Map<Byte,Integer> countSubordinateGroupByLevel(String userId);

    List<ShareBenefitRecord> queryByLevelAndUserId(String userId, Byte level,Integer pageSize,Integer pageNum);
}
