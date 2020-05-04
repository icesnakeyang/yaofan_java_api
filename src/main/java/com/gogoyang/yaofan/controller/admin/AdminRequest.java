package com.gogoyang.yaofan.controller.admin;

import lombok.Data;

import java.util.Date;

@Data
public class AdminRequest {
    private String loginName;
    private String password;
    private Integer pageIndex;
    private Integer pageSize;
    private String userId;
    private Date startTime;
    private Date endTime;
    private String action;
    private String actionId;
    private String taskId;
}
