package com.gogoyang.yaofan.utility.common;

import com.gogoyang.yaofan.meta.admin.entity.AdminInfo;
import com.gogoyang.yaofan.meta.admin.service.IAdminInfoService;
import com.gogoyang.yaofan.meta.task.entity.Task;
import com.gogoyang.yaofan.meta.task.service.ITaskService;
import com.gogoyang.yaofan.meta.team.entity.Team;
import com.gogoyang.yaofan.meta.team.entity.TeamUser;
import com.gogoyang.yaofan.meta.team.service.ITeamService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.meta.user.service.IUserInfoService;
import com.gogoyang.yaofan.meta.userActLog.entity.UserActLog;
import com.gogoyang.yaofan.meta.userActLog.service.IUserActLogService;
import com.gogoyang.yaofan.meta.volunteer.IVolunteerService;
import com.gogoyang.yaofan.meta.volunteer.task.entity.VolunteerTask;
import com.gogoyang.yaofan.utility.GogoActType;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.GogoTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CommonBusinessService implements ICommonBusinessService {
    private final IUserActLogService iUserActLogService;
    private final IUserInfoService iUserInfoService;
    private final ITaskService iTaskService;
    private final ITeamService iTeamService;
    private final IVolunteerService iVolunteerService;
    private final IAdminInfoService iAdminInfoService;

    public CommonBusinessService(IUserActLogService iUserActLogService,
                                 IUserInfoService iUserInfoService,
                                 ITaskService iTaskService,
                                 ITeamService iTeamService,
                                 IVolunteerService iVolunteerService,
                                 IAdminInfoService iAdminInfoService) {
        this.iUserActLogService = iUserActLogService;
        this.iUserInfoService = iUserInfoService;
        this.iTaskService = iTaskService;
        this.iTeamService = iTeamService;
        this.iVolunteerService = iVolunteerService;
        this.iAdminInfoService = iAdminInfoService;
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
        GogoStatus result=(GogoStatus)in.get("result");

        UserInfo userInfo = null;
        if (userId != null) {
            userInfo = getUserByUserId(userId);
        } else {
            if (token != null) {
                userInfo = getUserByToken(token);
            }else{
                if(memoMap!=null){
                    String memoStr=GogoTools.convertMapToString(memoMap);
                }
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
        userActLog.setResult(result.toString());
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

    @Override
    public AdminInfo getAdminByToken(String token) throws Exception {
        Map qIn=new HashMap();
        qIn.put("token", token);
        AdminInfo adminInfo=iAdminInfoService.getAdminInfo(qIn);
        if(adminInfo==null){
            throw new Exception("20024");
        }
        return adminInfo;
    }

    /**
     * 检查task是否有重复任务
     */
    @Override
    public boolean isDuplicateTask(Task task) throws Exception {
        /**
         * 如果createUserId相同，title相同，detail相同，status=GRABBING，teamId相同，即可判断为重复
         */

        Map qIn = new HashMap();
        qIn.put("title", task.getTitle());
        qIn.put("detail", task.getDetail());
        qIn.put("createUserId", task.getCreateUserId());
        qIn.put("status", GogoStatus.GRABBING.toString());

        Integer cc = iTaskService.totalTaskDuplicate(qIn);
        if (cc > 0) {
            //有内容相同的的记录
            return true;
        }
        return false;
    }

    /**
     * 根据teamId查找团队
     *
     * @param teamId
     * @return
     * @throws Exception
     */
    @Override
    public Team getTeamById(String teamId) throws Exception {
        Map qIn=new HashMap();
        qIn.put("teamId", teamId);
        Team team = iTeamService.getTeam(qIn);
        if (team == null) {
            throw new Exception("10014");
        }
        return team;
    }

    @Override
    public Task getTaskByTaskId(String taskId) throws Exception {
        Task task = iTaskService.getTaskByTaskId(taskId);
        if (task == null) {
            throw new Exception("10016");
        }
        return task;
    }

    /**
     * 检查用户是否是该团队成员
     *
     * @param userId
     * @param teamId
     * @return
     * @throws Exception
     */
    @Override
    public void checkUserTeam(String userId, String teamId) throws Exception {
        Map qIn=new HashMap();
        qIn.put("userId", userId);
        qIn.put("teamId", teamId);
        qIn.put("status", GogoStatus.ACTIVE.toString());
        ArrayList<TeamUser> teamUsers = iTeamService.listTeamUser(qIn);

        if (teamUsers.size()==0) {
            throw new Exception("10017");
        }
    }

    @Override
    public void checkTaskMember(UserInfo user, Task task) throws Exception {
        int cc = 0;
        if (user.getUserId().equals(task.getCreateUserId())) {
            cc++;
        }
        if (user.getUserId().equals(task.getPartyBId())) {
            cc++;
        }
        if (cc == 0) {
            throw new Exception("10020");
        }
    }

    /**
     * 查询义工任务简要信息
     * @param volunteerTaskId
     * @return
     * @throws Exception
     */
    @Override
    public VolunteerTask getVolunteerTask(String volunteerTaskId) throws Exception {
        VolunteerTask volunteerTask=iVolunteerService.getVolunteerTaskTiny(volunteerTaskId);
        if(volunteerTask==null){
            throw new Exception("20020");
        }
        return volunteerTask;
    }

    public UserInfo getUserByUserId(String userId) throws Exception {
        UserInfo userInfo = iUserInfoService.getUserInfoByUserId(userId);
        userInfo.setPassword(null);
        if (userInfo == null) {
            throw new Exception("10005");
        }
        return userInfo;

    }
}
