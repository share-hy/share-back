package com.share.hy.manager;

import com.share.hy.domain.ShareUser;

public interface IUserManager {

    ShareUser queryByUserNameAndPassword(String userName, String password);

    int countByUserName(String userName);

    void saveToken(String token,String userId);

    void newAddUser(ShareUser shareUser);

    ShareUser getAdminUser(String userId);
}
