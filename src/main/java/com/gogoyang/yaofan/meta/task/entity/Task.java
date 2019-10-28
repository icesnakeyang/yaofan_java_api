package com.gogoyang.yaofan.meta.task.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Task {
    private Integer ids;
    private String taskId;
    private String creatUserId;
    private Date createTime;
    private String title;
    private String detail;
    private Double point;
    private Date endTime;
}
