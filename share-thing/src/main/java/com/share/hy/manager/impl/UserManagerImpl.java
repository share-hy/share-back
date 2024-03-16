package com.share.hy.manager.impl;

import com.share.hy.common.constants.RedisKeyConstant;
import com.share.hy.common.enums.RoleEnum;
import com.share.hy.domain.ShareUser;
import com.share.hy.manager.IUserManager;
import com.share.hy.mapper.ShareUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.concurrent.TimeUnit;

@Component
public class UserManagerImpl implements IUserManager {

    private static final long TOKEN_EXPIRED_HOUR = 24;

    @Autowired
    private ShareUserMapper shareUserMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public ShareUser queryByUserNameAndPassword(String userName, String password) {
        Example example = new Example(ShareUser.class);
        example.createCriteria()
                .andEqualTo("userName",userName)
                .andEqualTo("password",password);
        return shareUserMapper.selectOneByExample(example);
    }

    @Override
    public int countByUserName(String userName) {
        Example example = new Example(ShareUser.class);
        example.createCriteria()
                .andEqualTo("userName",userName);
        return shareUserMapper.selectCountByExample(example);
    }

    @Override
    public void saveToken(String token,String userId) {
        String tokenKey = RedisKeyConstant.getUserTokenKey(token);
        stringRedisTemplate.opsForValue().set(tokenKey,userId,TOKEN_EXPIRED_HOUR, TimeUnit.HOURS);
    }

    @Override
    public void newAddUser(ShareUser shareUser) {
        shareUserMapper.insertSelective(shareUser);
    }

    @Override
    public ShareUser getAdminUser(String userId) {
        Example example = new Example(ShareUser.class);
        example.createCriteria().andEqualTo("userId",userId)
                .andEqualTo("role", RoleEnum.ADMIN.getCode());
        return shareUserMapper.selectOneByExample(example);
    }
}
