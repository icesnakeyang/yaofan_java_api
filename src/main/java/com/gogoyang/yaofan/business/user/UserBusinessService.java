package com.gogoyang.yaofan.business.user;

import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.meta.user.service.IUserInfoService;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.GogoTools;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserBusinessService implements IUserBusinessService {
    private final IUserInfoService iUserInfoService;
    private final ICommonBusinessService iCommonBusinessService;

    public UserBusinessService(IUserInfoService iUserInfoService,
                               ICommonBusinessService iCommonBusinessService) {
        this.iUserInfoService = iUserInfoService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map register(Map in) throws Exception {
        String phone = in.get("phone").toString();
        String password = in.get("password").toString();
        String name = (String) in.get("realName");

        /**
         * 1、查询phone是否已注册
         * 2、给password加密，MD5
         */
        UserInfo userInfo = iUserInfoService.getUserInfoByPhone(phone);
        if (userInfo != null) {
            throw new Exception("10002");
        }

        userInfo = new UserInfo();
        userInfo.setUserId(GogoTools.UUID().toString());
        userInfo.setCreateTime(new Date());
        password = GogoTools.encoderBySHA256(password);
        password = GogoTools.encoderByMd5(password);
        userInfo.setPassword(password);
        userInfo.setPhone(phone);
        userInfo.setName(name);
        userInfo.setStatus(GogoStatus.ACTIVE.toString());
        userInfo.setToken(GogoTools.UUID().toString());
        userInfo.setTokenTime(new Date());

        userInfo = iUserInfoService.createUserInfo(userInfo);

        Map out = new HashMap();
        out.put("userInfo", userInfo);
        return out;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map login(Map in) throws Exception {
        String phone = in.get("phone").toString();
        String password = in.get("password").toString();

        UserInfo userInfo = iUserInfoService.getUserInfoByPhone(phone);
        if (userInfo == null) {
            throw new Exception("10003");
        }
        password = GogoTools.encoderBySHA256(password);
        password = GogoTools.encoderByMd5(password);
        if (!userInfo.getPassword().equals(password)) {
            throw new Exception("10003");
        }

        Map out = new HashMap();
        out.put("userInfo", userInfo);

        return out;
    }

    @Override
    public Map loginByToken(Map in) throws Exception {
        String token = in.get("token").toString();

        UserInfo userInfo = iUserInfoService.getUserInfoByToken(token);

        Map out = new HashMap();
        out.put("userInfo", userInfo);
        return out;
    }

    @Override
    public Map updateUsername(Map in) throws Exception {
        String token = in.get("token").toString();
        String name = in.get("username").toString();
        String idCard=(String)in.get("idCard");

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        userInfo.setName(name);
        if(idCard!=null){
            userInfo.setIdCard(idCard);
        }

        iUserInfoService.updateUsername(userInfo);

        Map out = new HashMap();
        out.put("userInfo", userInfo);

        return out;
    }

    /**
     * 根据微信用户信息注册或者登录用户
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map wxLogin(Map in) throws Exception {
        String openId = in.get("openId").toString();
        String nickName = in.get("nickName").toString();
        String gender = in.get("gender").toString();
        String city = in.get("city").toString();
        String province = in.get("province").toString();
        String country = in.get("country").toString();

        /**
         * 通过openId查询用户记录
         */
        UserInfo userInfo = iUserInfoService.getUserInfoByOpenId(openId);
        if (userInfo == null) {
            /**
             * 没有查询到用户，创建一个用户
             */
            userInfo = new UserInfo();
            userInfo.setUserId(GogoTools.UUID().toString());
            userInfo.setToken(GogoTools.UUID().toString());
            userInfo.setCreateTime(new Date());
            userInfo.setTokenTime(new Date());
            userInfo.setStatus(GogoStatus.ACTIVE.toString());
            userInfo.setName(nickName);
            userInfo.setNickName(nickName);
            userInfo.setCity(city);
            userInfo.setGender(gender);
            userInfo.setProvince(province);
            userInfo.setCountry(country);
            userInfo.setOpenId(openId);
            iUserInfoService.createUserInfo(userInfo);
        }

        Map out = new HashMap();
        out.put("userInfo", userInfo);

        return out;
    }
}
