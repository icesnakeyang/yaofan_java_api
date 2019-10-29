package com.gogoyang.yaofan.controller.task;

import lombok.Data;

@Data
public class TaskRequest {
    private String detail;
    private String title;
    private String endTime;
    private String point;
    private String teamId;
    private String taskId;
}
