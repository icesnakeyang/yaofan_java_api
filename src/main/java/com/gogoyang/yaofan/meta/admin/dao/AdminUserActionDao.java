package com.gogoyang.yaofan.meta.admin.dao;

import com.gogoyang.yaofan.meta.userActLog.entity.UserActLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface AdminUserActionDao {

    /**
     * 查询用户的行为日志
     * @param qIn
     * userId
     * action
     * startDate
     * endDate
     * offset
     * size
     * @return
     */
    ArrayList<UserActLog> listUserAction(Map qIn);

    /**
     * 统计用户行为总数
     * @param qIn
     * userId
     * action
     * @return
     */
    Integer totalUserAction(Map qIn);

    /**
     * 读取用户行为日志的详情信息
     * @param qIn
     * actionId
     * @return
     */
    UserActLog getUserAction(Map qIn);
}
