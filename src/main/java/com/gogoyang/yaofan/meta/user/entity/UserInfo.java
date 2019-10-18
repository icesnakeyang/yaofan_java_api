package com.gogoyang.yaofan.meta.user.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserInfo {
    private String ids;
    private String userId;
    private String password;
    private Date createTime;
    private String token;
    private Date tokenTime;
    private String status;
    private String phone;
}