package com.share.hy.service;

import com.share.hy.common.enums.ErrorCodeEnum;
import com.share.hy.dto.console.AccountInfo;
import com.share.hy.dto.console.BalanceChangeDTO;
import com.share.hy.dto.user.UserAuthDTO;
import com.share.hy.dto.user.UserLoginDTO;

import java.util.List;

public interface IUserService {
    UserAuthDTO userLogin(UserLoginDTO userLoginDTO);

    ErrorCodeEnum register(UserLoginDTO userRegister) throws Exception;

    AccountInfo accountInfo(String userId);

    String queryUserAccount(String userId);

    List<BalanceChangeDTO> queryUserBalance(String userId);
}
