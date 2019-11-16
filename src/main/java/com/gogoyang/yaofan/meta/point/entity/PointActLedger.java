package com.gogoyang.yaofan.meta.point.entity;

import lombok.Data;

import java.util.Date;

@Data
public class PointActLedger {
    private Integer ids;
    private String pointActLedgerId;
    private Double point;
    private String actType;
    private String createUserId;
    private Date createTime;
    private String remark;
    private Date readTime;
    private String processUserId;
    private String processResult;
    private String processRemark;
    private Date processTime;
    private Date processReadTime;
}
