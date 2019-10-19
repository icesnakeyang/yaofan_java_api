package com.gogoyang.yaofan.meta.user.service;

import com.gogoyang.yaofan.meta.user.entity.UserInfo;

public interface IUserInfoService {
    UserInfo createUserInfo(UserInfo userInfo) throws Exception;

    UserInfo getUserInfoByPhone(String phone) throws Exception;

    UserInfo getUserInfoByToken(String token) throws Exception;

    UserInfo getUserInfoByUserId(String userId) throws Exception;
}
