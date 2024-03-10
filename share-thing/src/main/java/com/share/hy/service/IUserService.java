package com.share.hy.service;

import com.share.hy.common.enums.ErrorCodeEnum;
import com.share.hy.dto.user.UserAuthDTO;
import com.share.hy.dto.user.UserLoginDTO;

public interface IUserService {
    UserAuthDTO userLogin(UserLoginDTO userLoginDTO);

    ErrorCodeEnum register(UserLoginDTO userRegister);

    void logout(String userId);

    Object accountInfo(String userId);
}
