package com.gogoyang.yaofan.meta.userActLog.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserActLog {
    private Integer ids;

    /**
     * 发生时间
     */
    private Date createTime;

    /**
     * 日志Id
     */
    private String uuid;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 用户登录的地址
     */
    private String ipAddress;

    /**
     * 用户设备信息
     */
    private String device;

    /**
     * 用户设备系统
     */
    private String os;

    /**
     * 用户行为类型
     */
    private String action;

    private String result;


    /**
     * 需要记录的信息
     */
    private String memo;

    private String userName;

}
