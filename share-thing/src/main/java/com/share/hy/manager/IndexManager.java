package com.share.hy.manager;

import com.share.hy.domain.ShareIndexConfig;

import java.util.List;

public interface IndexManager {

    List<ShareIndexConfig> queryByType(Byte type);
}
