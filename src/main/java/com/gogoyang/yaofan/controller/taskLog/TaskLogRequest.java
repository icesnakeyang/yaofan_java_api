package com.gogoyang.yaofan.controller.taskLog;

import lombok.Data;

@Data
public class TaskLogRequest {
    private String taskId;
    private String createUserId;
    private String content;
}
