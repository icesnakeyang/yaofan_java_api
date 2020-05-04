package com.gogoyang.yaofan.meta.admin.service;

import com.gogoyang.yaofan.meta.admin.dao.AdminUserActionDao;
import com.gogoyang.yaofan.meta.userActLog.entity.UserActLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class AdminUserActionService implements IAdminUserActionService {
    private final AdminUserActionDao adminUserActionDao;

    public AdminUserActionService(AdminUserActionDao adminUserActionDao) {
        this.adminUserActionDao = adminUserActionDao;
    }

    /**
     * 查询用户的行为日志
     *
     * @param qIn userId
     *            action
     *            startDate
     *            endDate
     *            offset
     *            size
     * @return
     */
    @Override
    public ArrayList<UserActLog> listUserAction(Map qIn) {
        ArrayList<UserActLog> userActLogs = adminUserActionDao.listUserAction(qIn);
        return userActLogs;
    }

    /**
     * 统计用户行为总数
     *
     * @param qIn userId
     *            action
     * @return
     */
    @Override
    public Integer totalUserAction(Map qIn) {
        Integer total = adminUserActionDao.totalUserAction(qIn);
        return total;
    }

    /**
     * 读取用户行为日志的详情信息
     *
     * @param qIn actionId
     * @return
     */
    @Override
    public UserActLog getUserAction(Map qIn) throws Exception {
        UserActLog userActLog = adminUserActionDao.getUserAction(qIn);
        return userActLog;
    }
}
