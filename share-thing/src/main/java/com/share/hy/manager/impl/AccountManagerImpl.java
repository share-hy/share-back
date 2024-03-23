package com.share.hy.manager.impl;

import com.share.hy.domain.ShareUserAccount;
import com.share.hy.manager.IAccountManager;
import com.share.hy.mapper.ShareUserAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;

@Component
public class AccountManagerImpl implements IAccountManager {

    @Autowired
    private ShareUserAccountMapper shareUserAccountMapper;

    @Override
    public ShareUserAccount queryAccountByUserId(String userId) {
        Example example = new Example(ShareUserAccount.class);
        example.createCriteria().andEqualTo("userId",userId);
        return shareUserAccountMapper.selectOneByExample(example);
    }
}
