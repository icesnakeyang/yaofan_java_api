package com.gogoyang.yaofan.meta.admin.service;

import com.gogoyang.yaofan.meta.admin.dao.AdminInfoDao;
import com.gogoyang.yaofan.meta.admin.entity.AdminInfo;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdminInfoService implements IAdminInfoService{
    private final AdminInfoDao adminInfoDao;

    public AdminInfoService(AdminInfoDao adminInfoDao) {
        this.adminInfoDao = adminInfoDao;
    }

    @Override
    public void createAdminInfo(AdminInfo adminInfo) {
        adminInfoDao.createAdminInfo(adminInfo);
    }

    /**
     * 读取管理员用户信息
     * @param qIn
     * loginName
     * password
     * adminInfoId
     * token
     * @return
     */
    @Override
    public AdminInfo getAdminInfo(Map qIn) {
        AdminInfo adminInfo=adminInfoDao.getAdminInfo(qIn);
        return adminInfo;
    }
}
