package com.gogoyang.yaofan.business.user;

import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.meta.user.service.IUserInfoService;
import com.gogoyang.yaofan.utility.GogoTools;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
public class UserBusinessService implements IUserBusinessService {
    private final IUserInfoService iUserInfoService;

    public UserBusinessService(IUserInfoService iUserInfoService) {
        this.iUserInfoService = iUserInfoService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(Map in) throws Exception {
        String phone=in.get("phone").toString();
        String password=in.get("password").toString();

        /**
         * 1、查询phone是否已注册
         * 2、给password加密，MD5
         */
        UserInfo userInfo=iUserInfoService.getUserInfoByPhone(phone);
        if(userInfo!=null){
            throw new Exception("10002");
        }

        userInfo=new UserInfo();
        userInfo.setCreateTime(new Date());
        password=GogoTools.encoderBySHA256(password);
        password=GogoTools.encoderByMd5(password);
        userInfo.setPassword(password);
        userInfo.setPhone(phone);
        userInfo.setStatus();

        iUserInfoService.createUserInfo();
    }
}
