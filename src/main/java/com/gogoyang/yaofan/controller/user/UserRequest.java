package com.gogoyang.yaofan.controller.user;

import lombok.Data;

@Data
public class UserRequest {
    private String phone;
    private String password;
    private String token;
    private String username;
}
