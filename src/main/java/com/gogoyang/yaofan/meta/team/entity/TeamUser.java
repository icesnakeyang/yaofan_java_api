package com.gogoyang.yaofan.meta.team.entity;

import lombok.Data;

import java.util.Date;

/**
 * 团队用户的连接表
 */
@Data
public class TeamUser {
    private String ids;
    private String userId;
    private String teamId;
    private Date createTime;
    private String status;
    /**
     * 用户在团队里的角色类型
     */
    private String memberType;


    private String teamName;
    private String userName;
    private String managerId;
    private String managerName;
    private String creatorName;
    private String description;
    private Date teamCreateTime;
}
