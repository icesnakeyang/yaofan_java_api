package com.gogoyang.yaofan.meta.complete.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TaskComplete {
    private Integer ids;
    private String taskCompleteId;
    private String taskId;
    private String createUserId;
    private String createUserName;
    private Date createTime;
    private String content;
    private Date readTime;
    private String processUserId;
    private String processUserName;
    private String processResult;
    private String processRemark;
    private Date processTime;
    private Date processReadTime;
}
