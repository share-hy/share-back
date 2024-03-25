package com.share.hy.service.impl;

import com.share.hy.dto.console.EarningsOverviewDTO;
import com.share.hy.dto.console.SubordinateOverviewDTO;
import com.share.hy.service.IShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class IShareServiceImpl implements IShareService {



    @Override
    public Map<String, String> inviteLink(String userId) {
        return null;
    }

    @Override
    public EarningsOverviewDTO earningsOverview(String userId) {
        return null;
    }

    @Override
    public SubordinateOverviewDTO subordinateOverview(String userId) {

    }
}
