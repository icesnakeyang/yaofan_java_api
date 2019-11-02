package com.gogoyang.yaofan.business.task;

import com.gogoyang.yaofan.meta.task.entity.Task;
import com.gogoyang.yaofan.meta.task.service.ITaskService;
import com.gogoyang.yaofan.meta.team.entity.MyTeamView;
import com.gogoyang.yaofan.meta.team.entity.Team;
import com.gogoyang.yaofan.meta.team.entity.TeamView;
import com.gogoyang.yaofan.meta.team.service.ITeamService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.meta.user.service.IUserInfoService;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.GogoTools;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TaskBusinessService implements ITaskBusinessService {
    private final ICommonBusinessService iCommonBusinessService;
    private final IUserInfoService iUserInfoService;
    private final ITaskService iTaskService;
    private final ITeamService iTeamService;

    public TaskBusinessService(ICommonBusinessService iCommonBusinessService,
                               ITaskService iTaskService,
                               IUserInfoService iUserInfoService,
                               ITeamService iTeamService) {
        this.iCommonBusinessService = iCommonBusinessService;
        this.iTaskService = iTaskService;
        this.iUserInfoService = iUserInfoService;
        this.iTeamService = iTeamService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createTask(Map in) throws Exception {
        String token = in.get("token").toString();
        String detail = (String) in.get("detail");
        String title = in.get("title").toString();
        String endTimeStr = (String) in.get("endTime");
        String pointStr = (String) in.get("point");
        String teamId = (String) in.get("teamId");

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        TeamView teamView = null;
        if (teamId != null) {
            teamView = iCommonBusinessService.getTeamById(teamId);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(endTimeStr, pos);

        Double point = Double.parseDouble(pointStr);

        Task task = new Task();
        task.setCreateTime(new Date());
        task.setCreateUserId(userInfo.getUserId());
        task.setDetail(detail);
        task.setEndTime(strtodate);
        task.setPoint(point);
        task.setTaskId(GogoTools.UUID().toString());
        task.setTitle(title);
        task.setStatus(GogoStatus.BIDDING.toString());
        if (teamView != null) {
            task.setTeamId(teamView.getTeamId());
        }

        /**
         * 保存前先检查是否重复
         */
        if (iCommonBusinessService.isDuplicateTask(task)) {
            //重复了
            throw new Exception("10013");
        }
        iTaskService.createTask(task);
    }

    /**
     * 查询正在抢单的任务
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listBiddingTasks(Map in) throws Exception {
        /**
         * 查询条件：
         *  1、排除我创建的任务，userId
         *  2、状态为bidding
         *  3、属于我的团队
         */
        String token = in.get("token").toString();

        UserInfo userInfo = iUserInfoService.getUserInfoByToken(token);

        ArrayList<MyTeamView> myTeamViews = iTeamService.listTeam(userInfo.getUserId(), GogoStatus.ACTIVE.toString());

        Map qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        qIn.put("status", GogoStatus.BIDDING);
        if (myTeamViews.size() > 0) {
            ArrayList<String> teams = new ArrayList<>();
            for (int i = 0; i < myTeamViews.size(); i++) {
                teams.add(myTeamViews.get(i).getTeamId());
            }
            qIn.put("teams", teams);
        }

        ArrayList<Task> tasks = iTaskService.listTasks(qIn);

        Map out = new HashMap();
        out.put("tasks", tasks);
        return out;
    }

    @Override
    public Map getTaskByTaskId(Map in) throws Exception {
        String token = in.get("token").toString();
        String taskId = in.get("taskId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);
        ArrayList<MyTeamView> myTeamViews = iTeamService.listTeam(userInfo.getUserId(), GogoStatus.ACTIVE.toString());
        if (myTeamViews.size() == 0) {
            //没有加入团队
            throw new Exception("10015");
        }

        Task task = iTaskService.getTaskByTaskId(taskId);
        if (task == null) {
            throw new Exception("10016");
        }
        int cc = 0;
        for (int i = 0; i < myTeamViews.size(); i++) {
            if (myTeamViews.get(i).getTeamId().equals(task.getTeamId())) {
                cc++;
            }
        }
        if (cc == 0) {
            //用户不是团队成员
            throw new Exception("10017");
        }

        Map out = new HashMap();
        out.put("task", task);
        return out;
    }

    /**
     * 查询我的任务
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listMyTasks(Map in) throws Exception {
        /**
         * 1、进行中的任务，status=progress
         * 2、我是乙方，partyBId
         * 3、我是甲方，createUserId
         */
        String token = in.get("token").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("partyBId", userInfo.getUserId());

        qIn.put("partyAId", userInfo.getUserId());

        ArrayList<Task> tasks = iTaskService.listTasks(qIn);

        Map out = new HashMap();
        out.put("tasks", tasks);
        return out;
    }

    @Override
    public void grab(Map in) throws Exception {
        String token=in.get("token").toString();
        String taskId=in.get("taskId").toString();

        UserInfo userInfo=iCommonBusinessService.getUserByToken(token);
        Task task=iCommonBusinessService.getTaskByTaskId(taskId);

        if(!task.getStatus().equals(GogoStatus.PENDING.toString())){
            throw new Exception("10018");
        }
    }
}
