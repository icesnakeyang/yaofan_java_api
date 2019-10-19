package com.gogoyang.yaofan.utility.common;

import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.meta.user.service.IUserInfoService;
import com.gogoyang.yaofan.meta.userActLog.entity.UserActLog;
import com.gogoyang.yaofan.meta.userActLog.service.IUserActLogService;
import com.gogoyang.yaofan.utility.GogoActType;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.GogoTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CommonBusinessService implements ICommonBusinessService {
    private final IUserActLogService iUserActLogService;
    private final IUserInfoService iUserInfoService;

    public CommonBusinessService(IUserActLogService iUserActLogService,
                                 IUserInfoService iUserInfoService) {
        this.iUserActLogService = iUserActLogService;
        this.iUserInfoService = iUserInfoService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createUserActLog(Map in) throws Exception {
        GogoActType gogoActType = (GogoActType) in.get("GogoActType");
        String userId = (String) in.get("userId");
        String device = (String) in.get("device");
        String ipAddress = (String) in.get("ipAddress");
        HashMap memoMap = (HashMap) in.get("memo");
        String os = (String) in.get("os");
        String token = (String) in.get("token");

        UserInfo userInfo = null;
        if (userId != null) {
            userInfo = getUserByUserId(userId);
        } else {
            if (token != null) {
                userInfo = getUserByToken(token);
            }
        }

        UserActLog userActLog = new UserActLog();
        userActLog.setAction(gogoActType.toString());
        userActLog.setCreateTime(new Date());
        if (userInfo != null) {
            userActLog.setUserId(userInfo.getUserId());
        }
        userActLog.setDevice(device);
        userActLog.setIpAddress(ipAddress);
        userActLog.setMemo(GogoTools.convertMapToString(memoMap));
        userActLog.setOs(os);
        userActLog.setUuid(GogoTools.UUID().toString());
        iUserActLogService.createUserActLog(userActLog);
    }

    @Override
    public UserInfo getUserByToken(String token) throws Exception {
        UserInfo userInfo = iUserInfoService.getUserInfoByToken(token);
        if (userInfo == null) {
            throw new Exception("10004");
        }
        return userInfo;
    }

    private UserInfo getUserByUserId(String userId) throws Exception {
        UserInfo userInfo = iUserInfoService.getUserInfoByUserId(userId);
        if (userInfo == null) {
            throw new Exception("10005");
        }
        return userInfo;

    }
}
