package com.gogoyang.yaofan.meta.user.dao;

import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoDao {
    int createUserInfo(UserInfo userInfo);

    UserInfo getUserInfoByPhone(String phone);

    UserInfo getUserInfoByToken(String token);

    UserInfo getUserInfoByUserId(String userId);

    void updateUsername(UserInfo userInfo);

    /**
     * 根据用户的微信openId来查询用户注册信息
     * @param openId
     * @return
     */
    UserInfo getUserInfoByOpenId(String openId);
}
