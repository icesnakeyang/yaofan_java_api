package com.gogoyang.yaofan.utility.common;

import com.gogoyang.yaofan.utility.GogoActType;
import com.gogoyang.yaofan.utility.GogoStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class CommonBusinessService implements ICommonBusinessService {
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void logLogin(Map in) throws Exception {
        String phone = in.get("phone").toString();
        GogoActType actionType = (GogoActType) in.get("actionType");
        GogoStatus status = (GogoStatus) in.get("status");
        String message = (String) in.get("message");


    }
}
