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
    public void createUserInfo(UserInfo userInfo) throws Exception {

    }

    @Override
    public UserInfo getUserInfoByPhone(String phone) throws Exception {
        UserInfo userInfo = userInfoDao.getUserInfoByPhone(phone);
        return userInfo;
    }
}
