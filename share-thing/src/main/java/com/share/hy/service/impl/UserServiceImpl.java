package com.share.hy.service.impl;

import com.share.hy.common.CustomBusinessException;
import com.share.hy.common.constants.CookieConstant;
import com.share.hy.common.constants.UserConstant;
import com.share.hy.common.enums.ErrorCodeEnum;
import com.share.hy.domain.ShareUser;
import com.share.hy.domain.ShareUserTradeRecord;
import com.share.hy.dto.console.AccountInfo;
import com.share.hy.dto.console.BalanceChangeDTO;
import com.share.hy.dto.user.UserAuthDTO;
import com.share.hy.dto.user.UserLoginDTO;
import com.share.hy.manager.IAccountManager;
import com.share.hy.manager.IUserManager;
import com.share.hy.manager.IUserTradeRecordManager;
import com.share.hy.service.IUserService;
import com.share.hy.utils.Md5Util;
import com.share.hy.utils.SpringRequestHolderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserManager userManager;

    @Autowired
    private IUserTradeRecordManager userTradeRecordManager;

    @Override
    public UserAuthDTO userLogin(UserLoginDTO userLoginDTO) {
        ShareUser shareUser = userManager.queryByUserNameAndPassword(userLoginDTO.getUserName(),
                userLoginDTO.getPassword());
        if (null == shareUser){
            return null;
        }
        String userId = shareUser.getUserId();
        String token = generateToken();
        setCookie(token, userId);
        userManager.saveToken(token,userId);
        return new UserAuthDTO(userId,token,shareUser.getRole());
    }

    @Override
    public ErrorCodeEnum register(UserLoginDTO userRegister) throws Exception {
        int count = userManager.countByUserName(userRegister.getUserName());
        if (count > 0){
            return ErrorCodeEnum.ERROR_ACCOUNT_HAS_REGISTERED;
        }
        ShareUser shareUser = new ShareUser();
        shareUser.setPassword(Md5Util.MD5With32(userRegister.getPassword()));
        shareUser.setUserName(userRegister.getUserName());
        String userId = UserConstant.generateUserId();
        String token = generateToken();
        setCookie(token, userId);
        userManager.saveToken(token,userId);
        shareUser.setUserId(userId);
        userManager.newAddUser(shareUser);
        if (StringUtils.isNotBlank(userRegister.getInviteUserId())){
            userManager.linkUser(userId,userRegister.getInviteUserId());
        }
        return ErrorCodeEnum.SUCCESS;
    }


    @Override
    public AccountInfo accountInfo(String userId) {
        return null;
    }

    @Override
    public String queryUserAccount(String userId) {
        ShareUser shareUser = userManager.queryAccountByUserId(userId);
        if (null == shareUser){
            throw new CustomBusinessException(ErrorCodeEnum.ERROR_ORDER_NOT_EXIST);
        }
        return shareUser.getUserName();
    }

    @Override
    public List<BalanceChangeDTO> queryUserBalance(String userId) {
        List<ShareUserTradeRecord> userTradeRecords = userTradeRecordManager.queryBalanceByUserId(userId);
        if (CollectionUtils.isEmpty(userTradeRecords)){
            return Collections.emptyList();
        }
        return userTradeRecords.stream().map(BalanceChangeDTO::new).collect(Collectors.toList());
    }

    private void setCookie(String token,String userId){
        String domain = getDomain();
        CookieConstant.addTokenCookie(domain, token, CookieConstant.LOGIN_EXPIRED_TIME);
        CookieConstant.addUserIdCookie(domain, userId, CookieConstant.LOGIN_EXPIRED_TIME);
    }

    private String generateToken(){
        return UUID.randomUUID().toString().replace("-", "0");
    }

    protected String getDomain() {
        HttpServletRequest httpServletRequest = SpringRequestHolderUtil.getRequest();
        String domain = httpServletRequest.getHeader("Host");
        /*获取二级域名地址*/
        try {
            String[] domainValues = domain.split("\\.");
            if (domain.length() > 2) {
                domain = domainValues[1] + "." + domainValues[2];
            }

            if (domain.contains(":")) {
                domain = domain.split(":")[0];
            }
        } catch (Exception e) {
            log.warn("parser domain failed, domain:{}", domain);
        }
        return domain;
    }
}
