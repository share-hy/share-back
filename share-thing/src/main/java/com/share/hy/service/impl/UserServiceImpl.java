package com.share.hy.service.impl;

import com.share.hy.common.constants.CookieConstant;
import com.share.hy.common.constants.UserConstant;
import com.share.hy.common.enums.ErrorCodeEnum;
import com.share.hy.common.enums.RoleEnum;
import com.share.hy.domain.ShareUser;
import com.share.hy.dto.user.UserAuthDTO;
import com.share.hy.dto.user.UserLoginDTO;
import com.share.hy.manager.IUserManager;
import com.share.hy.service.IUserService;
import com.share.hy.utils.SpringRequestHolderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserManager userManager;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
        return new UserAuthDTO(userId,token);
    }

    @Override
    public ErrorCodeEnum register(UserLoginDTO userRegister) {
        int count = userManager.countByUserName(userRegister.getUserName());
        if (count > 0){
            return ErrorCodeEnum.ERROR_ACCOUNT_HAS_REGISTERED;
        }
        ShareUser shareUser = new ShareUser();
        shareUser.setPassword(userRegister.getPassword());
        shareUser.setUserName(userRegister.getUserName());
        String userId = UserConstant.generateUserId();
        String token = generateToken();
        setCookie(token, userId);
        userManager.saveToken(token,userId);
        shareUser.setUserId(userId);
        userManager.newAddUser(shareUser);
        return ErrorCodeEnum.SUCCESS;
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
