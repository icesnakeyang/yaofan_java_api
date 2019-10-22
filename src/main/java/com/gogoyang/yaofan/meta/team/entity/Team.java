package com.gogoyang.yaofan.meta.team.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Team {
    private Integer ids;
    private String teamId;
    /**
     * 团队名称
     */
    private String name;

    /**
     * 说明
     */
    private String description;
    private Date createTime;
    /**
     * 创建人Id
     */
    private String createUserId;
    /**
     * 管理员Id
     */
    private String managerId;
    /**
     * 当前状态
     */
    private String status;
}
