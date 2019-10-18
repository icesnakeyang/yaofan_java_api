package com.gogoyang.yaofan.meta.user.dao;

import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoDao {
    void createUserInfo(UserInfo userInfo);

    UserInfo getUserInfoByPhone(String phone);
}
