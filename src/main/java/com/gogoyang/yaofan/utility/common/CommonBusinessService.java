package com.gogoyang.yaofan.utility.common;

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

    public CommonBusinessService(IUserActLogService iUserActLogService) {
        this.iUserActLogService = iUserActLogService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createUserActLog(Map in) throws Exception {
        GogoActType gogoActType = (GogoActType) in.get("GogoActType");
        String userId = (String) in.get("userId");
        String device = (String) in.get("device");
        String ipAddress = (String) in.get("ipAddress");
        String memo = (String) in.get("memo");
        String os = (String) in.get("os");

        UserActLog userActLog = new UserActLog();
        userActLog.setAction(gogoActType.toString());
        userActLog.setCreateTime(new Date());
        userActLog.setUserId(userId);
        userActLog.setDevice(device);
        userActLog.setIpAddress(ipAddress);
        userActLog.setMemo(memo);
        userActLog.setOs(os);
        userActLog.setUuid(GogoTools.UUID().toString());
        iUserActLogService.createUserActLog(userActLog);
    }
}
