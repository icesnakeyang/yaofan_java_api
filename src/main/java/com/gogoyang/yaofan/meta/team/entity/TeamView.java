package com.gogoyang.yaofan.meta.team.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TeamView {
    private Integer ids;
    private String teamId;
    /**
     * 团队名称
     */
    private String teamName;
    /**
     * 说明
     */
    private String description;
    private Date createTime;
    /**
     * 创建人Id
     */
    private String createUserId;
    private String createUserName;
    /**
     * 管理员Id
     */
    private String managerId;
    private String managerName;
    /**
     * 当前状态
     */
    private String status;
}
