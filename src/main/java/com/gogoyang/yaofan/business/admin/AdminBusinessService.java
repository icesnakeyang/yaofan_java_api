package com.gogoyang.yaofan.business.admin;

import com.gogoyang.yaofan.meta.admin.entity.AdminInfo;
import com.gogoyang.yaofan.meta.admin.service.IAdminInfoService;
import com.gogoyang.yaofan.meta.admin.service.IAdminUserActionService;
import com.gogoyang.yaofan.meta.admin.service.IAdminUserService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.meta.userActLog.entity.UserActLog;
import com.gogoyang.yaofan.utility.GogoActType;
import com.gogoyang.yaofan.utility.GogoRole;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.GogoTools;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminBusinessService implements IAdminBusinessService {
    private final IAdminInfoService iAdminInfoService;
    private final ICommonBusinessService iCommonBusinessService;
    private final IAdminUserService iAdminUserService;
    private final IAdminUserActionService iAdminUserActionService;

    public AdminBusinessService(IAdminInfoService iAdminInfoService,
                                ICommonBusinessService iCommonBusinessService,
                                IAdminUserService iAdminUserService,
                                IAdminUserActionService iAdminUserActionService) {
        this.iAdminInfoService = iAdminInfoService;
        this.iCommonBusinessService = iCommonBusinessService;
        this.iAdminUserService = iAdminUserService;
        this.iAdminUserActionService = iAdminUserActionService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createAdmin(Map in) throws Exception {
//        String token=in.get("token").toString();
        String loginName = in.get("loginName").toString();
        String password = in.get("password").toString();

        AdminInfo adminInfo = new AdminInfo();

        adminInfo.setAdminInfoId(GogoTools.UUID().toString());
        adminInfo.setCreateTime(new Date());
        adminInfo.setLoginName(loginName);
        password = GogoTools.encoderByMd5(password);
        password = GogoTools.encoderBySHA256(password);
        adminInfo.setPassword(password);
        adminInfo.setRole(GogoRole.SUPER_ADMIN.toString());
        adminInfo.setStatus(GogoStatus.ACTIVE.toString());
        adminInfo.setToken(GogoTools.UUID().toString());
        adminInfo.setTokenTime(new Date());

        iAdminInfoService.createAdminInfo(adminInfo);
    }

    @Override
    public Map loginAdmin(Map in) throws Exception {
        String loginName = in.get("loginName").toString();
        String password = in.get("password").toString();

        password = GogoTools.encoderByMd5(password);
        password = GogoTools.encoderBySHA256(password);

        Map qIn = new HashMap();
        qIn.put("loginName", loginName);
        qIn.put("password", password);
        AdminInfo adminInfo = iAdminInfoService.getAdminInfo(qIn);

        adminInfo.setPassword(null);

        Map out = new HashMap();
        out.put("adminInfo", adminInfo);

        return out;
    }

    @Override
    public Map listRegisterUser(Map in) throws Exception {
        String token=in.get("token").toString();
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");

        AdminInfo adminInfo=iCommonBusinessService.getAdminByToken(token);

        Map qIn=new HashMap();
        Integer offset=(pageIndex-1)*pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<UserInfo> list=iAdminUserService.listRegisterUser(qIn);

        Map out=new HashMap();

        out.put("userInfos", list);

        /**
         * 统计注册用户总数
         */
        Integer totalRegisterUser=iAdminUserService.totalRegisterUser(qIn);
        out.put("totalRegisterUser", totalRegisterUser);

        return out;
    }

    @Override
    public Map getUser(Map in) throws Exception {
        String token=in.get("token").toString();
        String userId=in.get("userId").toString();

        AdminInfo adminInfo=iCommonBusinessService.getAdminByToken(token);

        UserInfo userInfo=iCommonBusinessService.getUserByUserId(userId);

        Map out=new HashMap();
        out.put("userInfo", userInfo);

        return out;

    }

    @Override
    public Map listUserAction(Map in) throws Exception {
        String token=in.get("token").toString();
        String userId=(String)in.get("userId");
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");

        AdminInfo adminInfo=iCommonBusinessService.getAdminByToken(token);

        Map qIn=new HashMap();

        Integer offset=(pageIndex-1)*pageSize;
        qIn.put("userId", userId);
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<UserActLog> userActLogs=iAdminUserActionService.listUserAction(qIn);

        Map out=new HashMap();
        out.put("userActLogs", userActLogs);

        Integer total=iAdminUserActionService.totalUserAction(qIn);
        out.put("totalUserAction", total);

        return out;
    }

    @Override
    public Map totalUserActionType(Map in) throws Exception {
        String token=in.get("token").toString();
        String  userId=(String)in.get("userId");

        AdminInfo adminInfo=iCommonBusinessService.getAdminByToken(token);

        Map out=new HashMap();

        Map qIn=new HashMap();
        qIn.put("userId", userId);

        //login
        qIn.put("action", GogoActType.WX_LOGIN);
        Integer total_WX_LOGIN=iAdminUserActionService.totalUserAction(qIn);
        out.put("total_WX_LOGIN", total_WX_LOGIN);

        qIn.put("action", GogoActType.CREATE_TASK);
        Integer total_CREATE_TASK=iAdminUserActionService.totalUserAction(qIn);
        out.put("total_CREATE_TASK", total_CREATE_TASK);


        return out;
    }
}
