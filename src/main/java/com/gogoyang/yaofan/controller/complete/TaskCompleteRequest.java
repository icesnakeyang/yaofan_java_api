package com.gogoyang.yaofan.controller.complete;

import lombok.Data;

@Data
public class TaskCompleteRequest {
    private String taskId;
    private String createUserId;
    private String content;
}
