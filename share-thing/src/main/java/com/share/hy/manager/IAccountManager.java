package com.share.hy.manager;

import com.share.hy.domain.ShareUserAccount;

import java.math.BigDecimal;

public interface IAccountManager {

    ShareUserAccount queryAccountByUserId(String userId);
}
