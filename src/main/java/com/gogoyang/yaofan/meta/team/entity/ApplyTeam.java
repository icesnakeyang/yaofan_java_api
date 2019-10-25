package com.gogoyang.yaofan.meta.team.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ApplyTeam {
    private Integer ids;
    /**
     * 申请的团队id
     */
    private String applyTeamId;
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
     * 处理结果
     */
    private String processResult;
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
