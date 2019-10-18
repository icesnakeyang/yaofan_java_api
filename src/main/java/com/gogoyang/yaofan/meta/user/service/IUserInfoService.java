package com.gogoyang.yaofan.meta.user.service;

import com.gogoyang.yaofan.meta.user.entity.UserInfo;

public interface IUserInfoService {
    void createUserInfo(UserInfo userInfo) throws Exception;

    UserInfo getUserInfoByPhone(String phone) throws Exception;
}
