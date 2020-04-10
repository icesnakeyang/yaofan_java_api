package com.gogoyang.yaofan.meta.user.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户信息
 */
@Data
public class UserInfo {
    private Integer ids;
    private String userId;
    private String password;
    private Date createTime;
    private String token;
    private Date tokenTime;
    private String status;
    private String phone;
    private String name;

    //微信信息
    private String openId;
    private String nickName;
    private String gender;
    private String city;
    private String province;
    private String country;
}