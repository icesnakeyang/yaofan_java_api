package com.gogoyang.yaofan.meta.volunteer.apply.entity;

import lombok.Data;

import java.util.Date;

/**
 * 义工申请类
 */
@Data
public class VolunteerApply {
    private Integer ids;
    private String volunteerApplyId;
    private String volunteerTaskId;
    private Date createTime;
    private String applyUserId;
    private String remark;
    private Date readTime;
    private String processResult;
    private Date processTime;
    private String processRemark;
    private String processUserId;
    private Date processReadTime;

    private String applyUserName;
    private String processUserName;
    private String title;
    private String taskCreateUserId;
    private String taskCreateUserName;
}
