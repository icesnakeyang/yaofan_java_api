package com.gogoyang.yaofan.utility.common;

import com.gogoyang.yaofan.meta.task.entity.Task;
import com.gogoyang.yaofan.meta.task.service.ITaskService;
import com.gogoyang.yaofan.meta.team.entity.MyTeamView;
import com.gogoyang.yaofan.meta.team.entity.Team;
import com.gogoyang.yaofan.meta.team.entity.TeamView;
import com.gogoyang.yaofan.meta.team.service.ITeamService;
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

    public CommonBusinessService(IUserActLogService iUserActLogService,
                                 IUserInfoService iUserInfoService,
                                 ITaskService iTaskService,
                                 ITeamService iTeamService) {
        this.iUserActLogService = iUserActLogService;
        this.iUserInfoService = iUserInfoService;
        this.iTaskService = iTaskService;
        this.iTeamService = iTeamService;
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

    /**
     * 检查task是否有重复任务
     */
    @Override
    public boolean isDuplicateTask(Task task) throws Exception {
        /**
         * 如果createUserId相同，title相同，detail相同，status=bidding，teamId相同，即可判断为重复
         */

        Map qIn = new HashMap();
        qIn.put("title", task.getTitle());
        qIn.put("detail", task.getDetail());
        qIn.put("createUserId", task.getCreateUserId());
        qIn.put("status", GogoStatus.BIDDING);

        Integer cc = iTaskService.totalTaskDuplicate(qIn);
        if (cc > 0) {
            //有内容相同的的记录
            return true;
        }
        return false;
    }

    /**
     * 根据teamId查找团队
     * @param teamId
     * @return
     * @throws Exception
     */
    @Override
    public TeamView getTeamById(String teamId) throws Exception {
        TeamView teamView = iTeamService.getTeamByTeamId(teamId);
        if(teamView==null){
            throw new Exception("10014");
        }
        return teamView;
    }

    @Override
    public Task getTaskByTaskId(String taskId) throws Exception {
        Task task=iTaskService.getTaskByTaskId(taskId);
        if(task==null){
            throw new Exception("10016");
        }
        return task;
    }

    /**
     * 检查用户是否是该团队成员
     * @param userId
     * @param teamId
     * @return
     * @throws Exception
     */
    @Override
    public void checkUserTeam(String userId, String teamId) throws Exception {
        ArrayList<MyTeamView> myTeamViews=iTeamService.listTeam(userId, GogoStatus.ACTIVE.toString());
        int cc=0;
        for(int i=0;i<myTeamViews.size();i++){
            if(myTeamViews.get(i).getTeamId().equals(teamId)){
                cc++;
            }
        }
        if(cc==0){
            throw new Exception("10017");
        }
    }

    private UserInfo getUserByUserId(String userId) throws Exception {
        UserInfo userInfo = iUserInfoService.getUserInfoByUserId(userId);
        if (userInfo == null) {
            throw new Exception("10005");
        }
        return userInfo;

    }
}
