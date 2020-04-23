package com.gogoyang.yaofan.meta.admin.entity;

import lombok.Data;

import java.util.Date;

/**
 * 管理员
 */
@Data
public class AdminInfo {
    private Integer ids;
    private String adminInfoId;
    private String loginName;
    private String password;
    private Date createTime;
    private String role;
    private String status;
    private String token;
    private Date tokenTime;
}
