package com.gogoyang.yaofan.meta.userActLog.dao;

import com.gogoyang.yaofan.meta.userActLog.entity.UserActLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserActLogDao {
    /**
     * 创建一条用户行为日志
     * @param userActLog
     */
    void createUserActLog(UserActLog userActLog);
}
