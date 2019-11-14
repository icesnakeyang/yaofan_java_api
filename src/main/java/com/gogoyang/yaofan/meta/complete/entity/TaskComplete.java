package com.gogoyang.yaofan.meta.complete.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TaskComplete {
    private Integer ids;
    private String taskCompleteId;
    private String taskId;
    private String createUserId;
    private Date createTime;
    private String content;
    private Date readTime;
    private String processUserId;
    private String processResult;
    private String processRemark;
    private String processTime;
    private Date processReadTime;
}
