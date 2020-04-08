package com.gogoyang.yaofan.meta.stop.entity;

import lombok.Data;

import java.util.Date;

/**
 * 停止任务日志
 */
@Data
public class TaskStop {
    private Integer ids;
    private String taskStopId;
    private String taskId;
    private String createUserId;
    private String remark;
    private Date createTime;
    private Date readTime;
    private String readUserId;

    private String createUserName;
    private String readUserName;
    private String taskName;
}
