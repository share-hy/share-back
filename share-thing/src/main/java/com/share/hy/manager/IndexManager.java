package com.share.hy.manager;

import com.share.hy.domain.ShareIndexConfig;
import com.share.hy.dto.user.IndexInfoDTO;

import java.util.List;

public interface IndexManager {

    List<ShareIndexConfig> queryByType(Byte type);
}
