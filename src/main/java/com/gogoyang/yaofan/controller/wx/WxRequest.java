package com.gogoyang.yaofan.controller.wx;

import lombok.Data;

@Data
public class WxRequest {
    private String code;
    private String encryptedData;
    private String iv;
    private String phone;
    private String os;

}
