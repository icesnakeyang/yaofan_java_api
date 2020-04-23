package com.gogoyang.yaofan.controller.admin;

import lombok.Data;

@Data
public class AdminRequest {
    private String loginName;
    private String password;
    private Integer pageIndex;
    private Integer pageSize;
    private String userId;
}
