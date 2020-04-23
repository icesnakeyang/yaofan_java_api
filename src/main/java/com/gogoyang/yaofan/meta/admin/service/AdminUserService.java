package com.gogoyang.yaofan.meta.admin.service;

import com.gogoyang.yaofan.meta.admin.dao.AdminUserDao;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class AdminUserService implements IAdminUserService{
    private final AdminUserDao adminUserDao;

    public AdminUserService(AdminUserDao adminUserDao) {
        this.adminUserDao = adminUserDao;
    }

    /**
     * 查询注册用户列表
     * @param qIn
     * offset
     * size
     * @return
     */
    @Override
    public ArrayList<UserInfo> listRegisterUser(Map qIn) {
        ArrayList<UserInfo> users=adminUserDao.listRegisterUser(qIn);
        return users;
    }

    /**
     * 统计注册用户总数
     * @param qIn
     * @return
     */
    @Override
    public Integer totalRegisterUser(Map qIn) {
        Integer total=adminUserDao.totalRegisterUser(qIn);
        return total;
    }

    /**
     * 查询一个用户的详情
     * @param qIn
     * userId
     * @return
     */
    @Override
    public UserInfo getUser(Map qIn) {
        UserInfo userInfo=adminUserDao.getUser(qIn);
        return userInfo;
    }
}
