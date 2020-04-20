package com.gogoyang.yaofan.meta.volunteer.task.entity;

import lombok.Data;

import java.util.Date;

/**
 * 义工任务类
 */
@Data
public class VolunteerTask {
    private Integer ids;
    private String volunteerTaskId;
    private Date createTime;
    private String createUserId;
    private String title;
    private String content;
    private Date startTime;
    private String pid;
    private Integer views;
    private Integer applies;
    private String status;

    private String createUserName;
}
