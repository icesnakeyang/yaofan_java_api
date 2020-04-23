package com.gogoyang.yaofan.meta.admin.dao;

import com.gogoyang.yaofan.meta.admin.entity.AdminInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface AdminInfoDao {
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
