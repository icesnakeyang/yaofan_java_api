package com.gogoyang.yaofan.meta.team.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TeamApplyLog {
    private Integer ids;
    /**
     * 申请事件Id
     */
    private String teamApplyLogId;
    /**
     * 申请的团队id
     */
    private String teamId;
    /**
     * 申请人Id
     */
    private String applyUserId;
    /**
     * 申请理由
     */
    private String applyRemark;
    /**
     * 申请时间
     */
    private Date createTime;
    /**
     * 处理申请的团队经理Id
     */
    private String processUserId;
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
}
