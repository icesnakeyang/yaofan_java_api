package com.gogoyang.yaofan.meta.userActLog.service;

import com.gogoyang.yaofan.meta.userActLog.dao.UserActLogDao;
import com.gogoyang.yaofan.meta.userActLog.entity.UserActLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserActLogService implements IUserActLogService {
    final
    UserActLogDao userActLogDao;

    public UserActLogService(UserActLogDao userActLogDao) {
        this.userActLogDao = userActLogDao;
    }

    @Override
    public void createUserActLog(UserActLog userActLog) throws Exception {
        userActLogDao.createUserActLog(userActLog);
    }
}
