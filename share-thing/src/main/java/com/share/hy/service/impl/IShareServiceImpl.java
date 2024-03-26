package com.share.hy.service.impl;

import com.share.hy.dto.console.EarningsOverviewDTO;
import com.share.hy.dto.console.SubordinateOverviewDTO;
import com.share.hy.service.IShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
public class IShareServiceImpl implements IShareService {

    @Autowired
    private Environment env;

    @Override
    public Map<String, String> inviteLink(String userId) {
        String link = env.getProperty("invite.link", String.class, "https://yhy-test.share.com?s=");
        return Collections.singletonMap("url",link + userId);
    }

    @Override
    public EarningsOverviewDTO earningsOverview(String userId) {

        return null;
    }

    @Override
    public SubordinateOverviewDTO subordinateOverview(String userId) {

    }
}
