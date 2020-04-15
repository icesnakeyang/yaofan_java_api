package com.gogoyang.yaofan.meta.user.service;

import com.gogoyang.yaofan.meta.user.entity.UserInfo;

public interface IUserInfoService {
    UserInfo createUserInfo(UserInfo userInfo) throws Exception;

    UserInfo getUserInfoByPhone(String phone) throws Exception;

    UserInfo getUserInfoByToken(String token) throws Exception;

    UserInfo getUserInfoByUserId(String userId) throws Exception;

    void updateUsername(UserInfo userInfo) throws Exception;

    /**
     * 根据用户的微信openId来查询用户注册信息
     * @param openId
     * @return
     * @throws Exception
     */
    UserInfo getUserInfoByOpenId(String openId) throws Exception;
}
