package com.gogoyang.yaofan.meta.point.entity;

import lombok.Data;

import java.util.Date;

@Data
public class PointLedger {
    private Integer ids;
    private String taskId;
    private Double pointIn;
    private Double pointOut;
    private String userId;
    private String userName;
    private String processUserId;
    private String processUserName;
    private Date createTime;
    private String remark;
    private String actType;
    private Date readTime;
    private String processResult;
    private String processRemark;
    private Date processTime;
    private Date processReadTime;
    private String pointLedgerId;
}
