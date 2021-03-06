package com.gogoyang.yaofan.meta.team.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户退团申请日志
 */
@Data
public class TeamQuitLog {
    private Integer ids;
    private String teamQuitLogId;
    /**
     * 申请的团队id
     */
    private String teamId;
    private String teamName;
    /**
     * 申请人Id
     */
    private String userId;
    private String userName;
    /**
     * 申请理由
     */
    private String remark;
    /**
     * 申请时间
     */
    private Date createTime;

    /**
     * 处理申请的团队经理Id
     */
    private String processUserId;
    private String processUserName;
    /**
     * 申请被团队经理阅读的时间
     */
    private Date readTime;
    /**
     * 日志状态
     */
    private String status;
    /**
     * 处理说明
     */
    private String processRemark;
    /**
     * 处理时间
     */
    private Date processTime;
    /**
     * 处理结果被申请人阅读的时间
     */
    private Date processReadTime;

    /**
     * 团队管理员
     */
    private String teamManagerId;
    private String teamManagerName;
}
