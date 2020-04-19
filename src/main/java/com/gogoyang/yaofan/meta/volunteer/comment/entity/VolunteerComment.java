package com.gogoyang.yaofan.meta.volunteer.comment.entity;

import lombok.Data;

import java.util.Date;

/**
 * 义工任务评价类
 */
@Data
public class VolunteerComment {
    private Integer ids;
    private String volunteerApplyId;
    private String comment;
    private Integer score;
    private Date createTime;
}
