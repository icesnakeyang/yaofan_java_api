package com.gogoyang.yaofan.meta.admin.dao;

import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface AdminUserDao {
    /**
     * 查询注册用户列表
     * @param qIn
     * offset
     * size
     * @return
     */
    ArrayList<UserInfo> listRegisterUser(Map qIn);

    /**
     * 统计注册用户总数
     * @param qIn
     * @return
     */
    Integer totalRegisterUser(Map qIn);

    /**
     * 查询一个用户的详情
     * @param qIn
     * userId
     * @return
     */
    UserInfo getUser(Map qIn);
}
