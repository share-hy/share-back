package com.share.hy.manager;

import com.share.hy.domain.ShareUser;
import com.share.hy.domain.ShareUserRelation;

public interface IUserManager {

    ShareUser queryByUserNameAndPassword(String userName, String password);

    int countByUserName(String userName);

    void saveToken(String token,String userId);

    void newAddUser(ShareUser shareUser);

    ShareUser getAdminUser(String userId);

    ShareUser queryAccountByUserId(String userId);

    void linkUser(String userId, String inviteUserId);

    ShareUserRelation queryRelationByUserId(String userId);
}
