package com.gogoyang.yaofan.meta.admin.service;

import com.gogoyang.yaofan.meta.admin.entity.AdminInfo;

import java.util.Map;

public interface IAdminInfoService {
    void createAdminInfo(AdminInfo adminInfo);

    /**
     * 读取管理员用户信息
     * @param qIn
     * loginName
     * password
     * adminInfoId
     * token
     * @return
     */
    AdminInfo getAdminInfo(Map qIn);
}
