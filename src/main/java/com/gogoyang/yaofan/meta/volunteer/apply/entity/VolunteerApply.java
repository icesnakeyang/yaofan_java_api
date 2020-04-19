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
    private Date create_time;
    private String applyUserId;
    private String remark;
    private Date readTime;
    private String processResult;
    private Date processTime;
    private String processRemark;
    private String processUserId;
    private Date processReadTime;
}
