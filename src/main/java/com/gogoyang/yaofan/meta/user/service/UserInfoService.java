package com.gogoyang.yaofan.meta.user.service;

import com.gogoyang.yaofan.meta.user.dao.UserInfoDao;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements IUserInfoService {
    private final UserInfoDao userInfoDao;

    public UserInfoService(UserInfoDao userInfoDao) {
        this.userInfoDao = userInfoDao;
    }

    @Override
    public UserInfo createUserInfo(UserInfo userInfo) throws Exception {
        userInfoDao.createUserInfo(userInfo);
        return userInfo;
    }

    @Override
    public UserInfo getUserInfoByPhone(String phone) throws Exception {
        UserInfo userInfo = userInfoDao.getUserInfoByPhone(phone);
        return userInfo;
    }

    @Override
    public UserInfo getUserInfoByToken(String token) throws Exception {
        UserInfo userInfo = userInfoDao.getUserInfoByToken(token);
        return userInfo;
    }
}
