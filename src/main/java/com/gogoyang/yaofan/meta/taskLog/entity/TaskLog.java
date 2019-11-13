package com.gogoyang.yaofan.meta.taskLog.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TaskLog {
    private Integer ids;
    private String taskLogId;
    private String taskId;
    private String createUserId;
    private String createUserName;
    private String content;
    private Date createTime;
    private Date readTime;
    private String readUserId;
    private String readUserName;
}
