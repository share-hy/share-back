package com.share.hy.manager.impl;

import com.alibaba.fastjson.JSONObject;
import com.share.hy.common.constants.RedisKeyConstant;
import com.share.hy.common.constants.UserConstant;
import com.share.hy.common.enums.RoleEnum;
import com.share.hy.domain.ShareUser;
import com.share.hy.domain.ShareUserRelation;
import com.share.hy.manager.IUserManager;
import com.share.hy.mapper.ShareUserMapper;
import com.share.hy.mapper.ShareUserRelationMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class UserManagerImpl implements IUserManager {

    private static final long TOKEN_EXPIRED_HOUR = 24;


    @Autowired
    private ShareUserMapper shareUserMapper;

    @Autowired
    private ShareUserRelationMapper relationMapper;

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

    @Override
    public ShareUser queryAccountByUserId(String userId) {
        Example example = new Example(ShareUser.class);
        example.createCriteria().andEqualTo("userId",userId);
        return shareUserMapper.selectOneByExample(example);
    }

    @Override
    public void linkUser(String userId, String inviteUserId) {
        ShareUserRelation shareUserRelation = new ShareUserRelation(userId,inviteUserId);
        ShareUserRelation userRelation = queryRelationByUserId(inviteUserId);
        if (null != userRelation){
            shareUserRelation.setSecond(userRelation.getFirst());
        }
        relationMapper.insertSelective(shareUserRelation);
        log.info("link user success:{}", JSONObject.toJSONString(shareUserRelation));
    }

    @Override
    public ShareUserRelation queryRelationByUserId(String userId) {
        Example example = new Example(ShareUserRelation.class);
        example.createCriteria().andEqualTo("userId",userId);
        return relationMapper.selectOneByExample(example);
    }
}
