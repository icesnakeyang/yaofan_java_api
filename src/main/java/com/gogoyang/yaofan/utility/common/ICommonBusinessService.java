package com.gogoyang.yaofan.utility.common;

import com.gogoyang.yaofan.meta.user.entity.UserInfo;

import java.util.Map;

public interface ICommonBusinessService {
    void createUserActLog(Map in) throws Exception;

    UserInfo getUserByToken(String token) throws Exception;
}
