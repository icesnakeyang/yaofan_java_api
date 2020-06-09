package com.gogoyang.yaofan.utility.common;

import com.gogoyang.yaofan.meta.admin.entity.AdminInfo;
import com.gogoyang.yaofan.meta.admin.service.IAdminInfoService;
import com.gogoyang.yaofan.meta.fileLog.entity.FileLog;
import com.gogoyang.yaofan.meta.fileLog.service.IFileLogService;
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
import com.gogoyang.yaofan.utility.GogoRole;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.GogoTools;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final IFileLogService iFileLogService;

    public CommonBusinessService(IUserActLogService iUserActLogService,
                                 IUserInfoService iUserInfoService,
                                 ITaskService iTaskService,
                                 ITeamService iTeamService,
                                 IVolunteerService iVolunteerService,
                                 IAdminInfoService iAdminInfoService,
                                 IFileLogService iFileLogService) {
        this.iUserActLogService = iUserActLogService;
        this.iUserInfoService = iUserInfoService;
        this.iTaskService = iTaskService;
        this.iTeamService = iTeamService;
        this.iVolunteerService = iVolunteerService;
        this.iAdminInfoService = iAdminInfoService;
        this.iFileLogService = iFileLogService;
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
            //给游客一个体验账号

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
         * 再加上point相同
         */

        Map qIn = new HashMap();
        qIn.put("title", task.getTitle());
        qIn.put("detail", task.getDetail());
        qIn.put("createUserId", task.getCreateUserId());
        qIn.put("status", GogoStatus.GRABBING.toString());
        qIn.put("point", task.getPoint());

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
            //任务创建人，甲方
            cc++;
        }
        if (user.getUserId().equals(task.getPartyBId())) {
            //任务承接人，乙方
            cc++;
        }

        if (cc == 0) {
            /**
             * 如果既不是甲方，也不是一方，就检查是否为该任务团队的观察者，
             * 或者用户是团队管理员
             */
            Map qIn=new HashMap();
            qIn.put("teamId", task.getTeamId());
            Team team=iTeamService.getTeam(qIn);
            if(!team.getManagerId().equals(user.getUserId())){
                qIn=new HashMap();
                qIn.put("teamId", task.getTeamId());
                qIn.put("userId", user.getUserId());
                ArrayList<TeamUser> teamUsers=iTeamService.listTeamUser(qIn);
                if(teamUsers.size()==1){
                    if(!teamUsers.get(0).getMemberType().equals(GogoRole.TEAM_OBSERVER.toString())){
                        throw new Exception("10020");
                    }
                }else {
                    throw new Exception("10020");
                }
            }
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

    /**
     * 创建一个上传文件日志
     * 保存上传文件url地址
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map createFileLog(Map in) throws Exception {
        String token=in.get("token").toString();
        String fileName=in.get("fileName").toString();
        String url=in.get("url").toString();

        /**
         * 获取用户
         * 这里用户可能是普通用户或者管理员
         * 首先查询UserInfo,如果没有，再查询AdminInfo
         */
        UserInfo userInfo=getUserByToken(token);

        FileLog fileLog=new FileLog();
        fileLog.setCreateTime(new Date());
        fileLog.setFileLogId(GogoTools.UUID().toString());
        fileLog.setFilename(fileName);
        fileLog.setUserId(userInfo.getUserId());
        fileLog.setUrl(url);

        iFileLogService.createFileLog(fileLog);

        Map out=new HashMap();
        out.put("fileLogId", fileLog.getFileLogId());
        out.put("url", fileLog.getUrl());

        return out;
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
