package com.gogoyang.yaofan.meta.taskLog.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TaskLog {
    private Integer ids;
    private String taskLogId;
    private String taskId;
    private String createUserId;
    private String content;
    private Date createTime;
    private Date readTime;
    private String processUserId;
    private String processRemark;
    private String processTime;
    private String processReadTime;
}
