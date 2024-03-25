package com.share.hy.service;

import com.share.hy.common.ResponseMsg;
import com.share.hy.dto.console.EarningsOverviewDTO;
import com.share.hy.dto.console.SubordinateOverviewDTO;

import java.util.Map;

public interface IShareService {

    Map<String,String> inviteLink(String userId);

    EarningsOverviewDTO earningsOverview(String userId);

    SubordinateOverviewDTO subordinateOverview(String userId);
}
