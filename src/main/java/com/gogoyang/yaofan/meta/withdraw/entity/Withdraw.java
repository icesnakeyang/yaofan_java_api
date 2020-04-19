package com.gogoyang.yaofan.meta.withdraw.entity;

import lombok.Data;

import java.util.Date;

/**
 * 取现申请
 */
@Data
public class Withdraw {
    private Integer ids;
    private String withdrawId;
    private Double point;
    private String userId;
    private Date createTime;
    private String remark;
    private Date readTime;
    private String processResult;
    private String processRemark;
    private String processUserId;
    private Date processTime;
    private Date processReadTime;

    private String userName;
    private String processUserName;
}
