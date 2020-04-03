package com.gogoyang.yaofan.controller.task;

import lombok.Data;

@Data
public class TaskRequest {
    private String detail;
    private String title;
    //微信小程序使用endDate和endTime2来组合生成任务截止时间
    private String endDateWx;
    private String endTimeWx;
    //手机版用endTime
    private String endTime;
    private String point;
    private String teamId;
    private String taskId;
    private Integer pageIndex;
    private Integer pageSize;
    private String status;
}
