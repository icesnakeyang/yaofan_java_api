package com.gogoyang.yaofan.business.task;

import com.gogoyang.yaofan.meta.complete.service.ITaskCompleteService;
import com.gogoyang.yaofan.meta.point.entity.PointLedger;
import com.gogoyang.yaofan.meta.point.service.IPointService;
import com.gogoyang.yaofan.meta.task.entity.Task;
import com.gogoyang.yaofan.meta.task.service.ITaskService;
import com.gogoyang.yaofan.meta.taskLog.service.ITaskLogService;
import com.gogoyang.yaofan.meta.team.entity.MyTeamView;
import com.gogoyang.yaofan.meta.team.entity.TeamView;
import com.gogoyang.yaofan.meta.team.service.ITeamService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.meta.user.service.IUserInfoService;
import com.gogoyang.yaofan.utility.GogoActType;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.GogoTools;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TaskBusinessService implements ITaskBusinessService {
    private final ICommonBusinessService iCommonBusinessService;
    private final IUserInfoService iUserInfoService;
    private final ITaskService iTaskService;
    private final ITeamService iTeamService;
    private final IPointService iPointService;
    private final ITaskLogService iTaskLogService;
    private final ITaskCompleteService iTaskCompleteService;

    public TaskBusinessService(ICommonBusinessService iCommonBusinessService,
                               ITaskService iTaskService,
                               IUserInfoService iUserInfoService,
                               ITeamService iTeamService,
                               IPointService iPointService,
                               ITaskLogService iTaskLogService,
                               ITaskCompleteService iTaskCompleteService) {
        this.iCommonBusinessService = iCommonBusinessService;
        this.iTaskService = iTaskService;
        this.iUserInfoService = iUserInfoService;
        this.iTeamService = iTeamService;
        this.iPointService = iPointService;
        this.iTaskLogService = iTaskLogService;
        this.iTaskCompleteService = iTaskCompleteService;
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

        ArrayList<Task> tasks = iTaskService.listBiddingTasks(qIn);

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

        /**
         * 读取task的统计信息
         */
        //日志总数
        Integer totalTaskLog = iTaskLogService.totalTaskLog(task.getTaskId());
        out.put("totalTaskLog", totalTaskLog);

        //未阅读的日志总数
        Integer totalUnreadTaskLog = iTaskLogService.totalTaskLogUnread(task.getTaskId(), userInfo.getUserId());
        out.put("totalUnreadTaskLog", totalUnreadTaskLog);

        //完成日志总数
        Integer totalTaskComplete=iTaskCompleteService.totalTaskComplete(task.getTaskId());
        out.put("totalTaskComplete", totalTaskComplete);

        Integer totalUnreadComplete = iTaskCompleteService.totalCompleteUnread(task.getTaskId(), userInfo.getUserId());
        out.put("totalUnreadTaskComplete", totalUnreadComplete);
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
        qIn.put("userId", userInfo.getUserId());

        ArrayList<Task> tasks = iTaskService.listMyTasks(qIn);

        /**
         * 读取task的统计信息
         */
        ArrayList list = new ArrayList();
        for (int i = 0; i < tasks.size(); i++) {
            Map map = new HashMap();
            map.put("task", tasks.get(i));
            //日志总数
            Integer totalTaskLog = iTaskLogService.totalTaskLog(tasks.get(i).getTaskId());
            map.put("totalTaskLog", totalTaskLog);

            //未阅读的日志总数
            Integer totalUnreadTaskLog = iTaskLogService.totalTaskLogUnread(tasks.get(i).getTaskId(), userInfo.getUserId());
            map.put("totalUnreadTaskLog", totalUnreadTaskLog);

            //未阅读的完成日志
            Integer totalUnreadComplete = iTaskCompleteService.totalCompleteUnread(tasks.get(i).getTaskId(), userInfo.getUserId());
            map.put("totalUnreadTaskComplete", totalUnreadComplete);

            list.add(map);
        }
        Map out = new HashMap();
        out.put("tasks", list);
        return out;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void grab(Map in) throws Exception {
        String token = in.get("token").toString();
        String taskId = in.get("taskId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);
        Task task = iCommonBusinessService.getTaskByTaskId(taskId);

        /**
         * 任务必须是bidding状态
         */
        if (!task.getStatus().equals(GogoStatus.BIDDING.toString())) {
            throw new Exception("10018");
        }

        /**
         * 不能接自己创建的任务
         */
        if (task.getCreateUserId().equals(userInfo.getUserId())) {
            throw new Exception("10019");
        }

        /**
         * 任务必须是自己的团队的任务
         */
        iCommonBusinessService.checkUserTeam(userInfo.getUserId(), task.getTeamId());

        /**
         * 修改任务状态
         */
        iTaskService.updateTaskDeal(taskId, userInfo.getUserId());

        /**
         * 记录积分账
         */
        PointLedger pointLedger = new PointLedger();
        pointLedger.setActType(GogoActType.GRAB.toString());
        pointLedger.setCreateTime(new Date());
        pointLedger.setPointIn(task.getPoint());
        pointLedger.setTaskId(task.getTaskId());
        pointLedger.setUserId(userInfo.getUserId());
        iPointService.createPointLedger(pointLedger);
    }
}
