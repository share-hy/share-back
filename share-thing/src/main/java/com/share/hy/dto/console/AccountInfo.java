package com.share.hy.dto.console;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class AccountInfo {

    private String userName;

    private BigDecimal balance;

    private String currentService;

    private String dueDate;

    private String vipLevel;

    private Map<String,String> vipLevelDesc;
}
