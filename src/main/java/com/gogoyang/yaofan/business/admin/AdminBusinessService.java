package com.gogoyang.yaofan.business.admin;

import com.gogoyang.yaofan.meta.admin.entity.AdminInfo;
import com.gogoyang.yaofan.meta.admin.service.IAdminInfoService;
import com.gogoyang.yaofan.meta.admin.service.IAdminUserActionService;
import com.gogoyang.yaofan.meta.admin.service.IAdminUserService;
import com.gogoyang.yaofan.meta.task.entity.Task;
import com.gogoyang.yaofan.meta.task.service.ITaskService;
import com.gogoyang.yaofan.meta.taskLog.entity.TaskLog;
import com.gogoyang.yaofan.meta.taskLog.service.ITaskLogService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.meta.userActLog.entity.UserActLog;
import com.gogoyang.yaofan.utility.GogoActType;
import com.gogoyang.yaofan.utility.GogoRole;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.GogoTools;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.hibernate.validator.constraints.br.CNPJ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AdminBusinessService implements IAdminBusinessService {
    private final IAdminInfoService iAdminInfoService;
    private final ICommonBusinessService iCommonBusinessService;
    private final IAdminUserService iAdminUserService;
    private final IAdminUserActionService iAdminUserActionService;
    private final ITaskService iTaskService;
    private final ITaskLogService iTaskLogService;

    public AdminBusinessService(IAdminInfoService iAdminInfoService,
                                ICommonBusinessService iCommonBusinessService,
                                IAdminUserService iAdminUserService,
                                IAdminUserActionService iAdminUserActionService,
                                ITaskService iTaskService,
                                ITaskLogService iTaskLogService) {
        this.iAdminInfoService = iAdminInfoService;
        this.iCommonBusinessService = iCommonBusinessService;
        this.iAdminUserService = iAdminUserService;
        this.iAdminUserActionService = iAdminUserActionService;
        this.iTaskService = iTaskService;
        this.iTaskLogService = iTaskLogService;
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
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        AdminInfo adminInfo = iCommonBusinessService.getAdminByToken(token);

        Map qIn = new HashMap();
        Integer offset = (pageIndex - 1) * pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<UserInfo> list = iAdminUserService.listRegisterUser(qIn);

        Map out = new HashMap();

        out.put("userInfos", list);

        /**
         * 统计注册用户总数
         */
        Integer totalRegisterUser = iAdminUserService.totalRegisterUser(qIn);
        out.put("totalRegisterUser", totalRegisterUser);

        return out;
    }

    @Override
    public Map getUser(Map in) throws Exception {
        String token = in.get("token").toString();
        String userId = in.get("userId").toString();

        AdminInfo adminInfo = iCommonBusinessService.getAdminByToken(token);

        UserInfo userInfo = iCommonBusinessService.getUserByUserId(userId);

        Map out = new HashMap();
        out.put("userInfo", userInfo);

        return out;

    }

    @Override
    public Map listUserAction(Map in) throws Exception {
        String token = in.get("token").toString();
        String userId = (String) in.get("userId");
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");
        Date startTime = (Date) in.get("startTime");
        Date endTime = (Date) in.get("endTime");
        String action = (String) in.get("action");

        AdminInfo adminInfo = iCommonBusinessService.getAdminByToken(token);

        Map qIn = new HashMap();

        Integer offset = (pageIndex - 1) * pageSize;
        qIn.put("userId", userId);
        qIn.put("offset", offset);
        qIn.put("size", pageSize);

        if (startTime != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            startTime = calendar.getTime();
        }
        if(endTime!=null){
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(endTime);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            endTime=calendar.getTime();
        }

        qIn.put("startDate", startTime);
        qIn.put("endDate", endTime);
        qIn.put("action", action);
        ArrayList<UserActLog> userActLogs = iAdminUserActionService.listUserAction(qIn);


        Map out = new HashMap();
        out.put("userActLogs", userActLogs);

        Integer total = iAdminUserActionService.totalUserAction(qIn);
        out.put("totalUserAction", total);

        return out;
    }

    @Override
    public Map totalUserActionType(Map in) throws Exception {
        String token = in.get("token").toString();
        String userId = (String) in.get("userId");

        AdminInfo adminInfo = iCommonBusinessService.getAdminByToken(token);

        Map out = new HashMap();

        Map qIn = new HashMap();
        qIn.put("userId", userId);

        //统计总共的微信登录次数
        qIn.put("action", GogoActType.WX_LOGIN);
        Integer total_WX_LOGIN = iAdminUserActionService.totalUserAction(qIn);
        out.put("total_WX_LOGIN", total_WX_LOGIN);

        //统计总共创建的任务总数
        qIn.put("action", GogoActType.CREATE_TASK);
        Integer total_CREATE_TASK = iAdminUserActionService.totalUserAction(qIn);
        out.put("total_CREATE_TASK", total_CREATE_TASK);

        //今天
        Date today = new Date();


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        today=calendar.getTime();

        int day = calendar.get(Calendar.DATE);
        //                      此处修改为+1则是获取后一天

        //明天
        calendar.set(Calendar.DATE, day+1);
        Date nextDay=calendar.getTime();

        //昨天
        calendar.set(Calendar.DATE, day - 1);
        Date lastDay=calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        //统计今天的登录人数
        qIn.put("action", GogoActType.WX_LOGIN);
        qIn.put("startDate", today);
        qIn.put("endDate", nextDay);
        Integer total_WX_LOGIN_today = iAdminUserActionService.totalUserAction(qIn);
        out.put("total_WX_LOGIN_today", total_WX_LOGIN_today);
        ArrayList<UserActLog> userActLogs = iAdminUserActionService.listUserAction(qIn);

        //今日任务数
        qIn.put("action", GogoActType.CREATE_TASK);
        Integer total_CREATE_TASK_today = iAdminUserActionService.totalUserAction(qIn);
        out.put("total_CREATE_TASK_today", total_CREATE_TASK_today);

        //统计昨天的登录人数
        qIn.put("action", GogoActType.WX_LOGIN);
        qIn.put("startDate", lastDay);
        qIn.put("endDate", nextDay);
        Integer total_WX_LOGIN_lastday=iAdminUserActionService.totalUserAction(qIn);
        out.put("total_WX_LOGIN_lastday", total_WX_LOGIN_lastday);

        //昨日任务数
        qIn.put("action", GogoActType.CREATE_TASK);
        Integer total_CREATE_TASK_lastday=iAdminUserActionService.totalUserAction(qIn);
        out.put("total_CREATE_TASK_lastday", total_CREATE_TASK_lastday);

        //统计本周登录
        Date week1=GogoTools.getBeginDayOfWeek();
        Date week2=GogoTools.getEndDayOfWeek();
        qIn.put("startDate", week1);
        qIn.put("endDate",today);
        qIn.put("action", GogoActType.WX_LOGIN);
        Integer total_WX_LOGIN_thisweek=iAdminUserActionService.totalUserAction(qIn);
        out.put("total_WX_LOGIN_thisweek",total_WX_LOGIN_thisweek);

        //本周任务
        qIn.put("action", GogoActType.CREATE_TASK);
        Integer total_CREATE_TASK_thisweek=iAdminUserActionService.totalUserAction(qIn);
        out.put("total_CREATE_TASK_thisweek", total_CREATE_TASK_thisweek);

        //统计本月
        //本月任务
        Date month1 = GogoTools.dateStartOfThisMonth();
        qIn.put("startDate", month1);
        Integer total_CREATE_TASK_thismonth=iAdminUserActionService.totalUserAction(qIn);
        out.put("total_CREATE_TASK_thismonth", total_CREATE_TASK_thismonth);

        //本月登录
        qIn.put("action", GogoActType.WX_LOGIN);
        Integer total_WX_LOGIN_thismonth=iAdminUserActionService.totalUserAction(qIn);
        out.put("total_WX_LOGIN_thismonth", total_WX_LOGIN_thismonth);

        //获取本月每天的日期
        Date lastDate=GogoTools.getBeginDayOfLastMonth();
        ArrayList dateList=listDateOfMonth(lastDate);

        qIn=new HashMap();
        qIn.put("action", GogoActType.WX_LOGIN);
        ArrayList monthDays=new ArrayList();
        for(int i=0;i<dateList.size();i++) {
            qIn.put("startDate", dateList.get(i));
            if(i<dateList.size()-1) {
                qIn.put("endDate", dateList.get(i + 1));
            }else {
                calendar.setTime((Date)dateList.get(i));
                calendar.add(Calendar.DATE,1);
                Date endDate=GogoTools.setDateTimeTo0(calendar.getTime());
                qIn.put("endDate", endDate);
            }
            Integer t=iAdminUserActionService.totalUserAction(qIn);
            Map monthdayTotalMap=new HashMap();
            monthdayTotalMap.put("day"+String.valueOf(i).toString(), t);
            monthDays.add(monthdayTotalMap);
        }
        out.put("monthDays", monthDays);


        return out;
    }

    @Override
    public Map getUserAction(Map in) throws Exception {
        String token=in.get("token").toString();
        String actionId=in.get("actionId").toString();

        AdminInfo adminInfo=iCommonBusinessService.getAdminByToken(token);

        Map qIn=new HashMap();
        qIn.put("actionId", actionId);
        UserActLog userActLog=iAdminUserActionService.getUserAction(qIn);

        Map out=new HashMap();
        out.put("action", userActLog);

        return out;
    }

    @Override
    public Map getTask(Map in) throws Exception {
        String token=in.get("token").toString();
        String taskId=in.get("taskId").toString();

        AdminInfo adminInfo=iCommonBusinessService.getAdminByToken(token);
        Task task=iTaskService.getTaskByTaskId(taskId);

        Map out=new HashMap();
        out.put("task", task);

        //查询任务日志
        Map qIn=new HashMap();
        qIn.put("taskId", task.getTaskId());
        ArrayList<TaskLog> taskLogs=iTaskLogService.listTaskLog(qIn);
        out.put("taskLogs", taskLogs);

        return out;

    }

    private ArrayList listDateOfMonth(Date date) throws Exception{
        //获取指定月的开始日期
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE,1);
        Date startDate=calendar.getTime();

        /**
         * 获取指定月的结束日期
         * 如果是本月，就截止今天，如果不是，就到该月最后一天
         */
        //首先计算该月最后一天
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = calendar.getTime();
        int endDay=calendar.get(Calendar.DATE);
        int theMonth=calendar.get(Calendar.MONTH);
        ArrayList dateList=new ArrayList();

        Date theDate=startDate;
        int theDay=1;
        while (theDay<=endDay){
            calendar.setTime(theDate);
            theDate=GogoTools.setDateTimeTo0(theDate);
            dateList.add(theDate);
            calendar.add(Calendar.DATE,1);
            if(calendar.get(Calendar.MONTH)!=theMonth){
                break;
            }
            theDate=calendar.getTime();
            theDay=calendar.get(Calendar.DATE);
        }

        return dateList;
    }
}
