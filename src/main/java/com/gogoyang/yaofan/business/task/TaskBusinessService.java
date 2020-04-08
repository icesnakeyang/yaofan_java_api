package com.gogoyang.yaofan.business.task;

import com.gogoyang.yaofan.meta.complete.service.ITaskCompleteService;
import com.gogoyang.yaofan.meta.point.entity.PointLedger;
import com.gogoyang.yaofan.meta.point.service.IPointService;
import com.gogoyang.yaofan.meta.stop.service.ITaskStopService;
import com.gogoyang.yaofan.meta.task.entity.Task;
import com.gogoyang.yaofan.meta.task.service.ITaskService;
import com.gogoyang.yaofan.meta.taskLog.service.ITaskLogService;
import com.gogoyang.yaofan.meta.team.entity.Team;
import com.gogoyang.yaofan.meta.team.entity.TeamUser;
import com.gogoyang.yaofan.meta.team.service.ITeamService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.meta.user.service.IUserInfoService;
import com.gogoyang.yaofan.utility.GogoActType;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.GogoTools;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
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
    private final ITaskStopService iTaskStopService;

    public TaskBusinessService(ICommonBusinessService iCommonBusinessService,
                               ITaskService iTaskService,
                               IUserInfoService iUserInfoService,
                               ITeamService iTeamService,
                               IPointService iPointService,
                               ITaskLogService iTaskLogService,
                               ITaskCompleteService iTaskCompleteService,
                               ITaskStopService iTaskStopService) {
        this.iCommonBusinessService = iCommonBusinessService;
        this.iTaskService = iTaskService;
        this.iUserInfoService = iUserInfoService;
        this.iTeamService = iTeamService;
        this.iPointService = iPointService;
        this.iTaskLogService = iTaskLogService;
        this.iTaskCompleteService = iTaskCompleteService;
        this.iTaskStopService = iTaskStopService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createTask(Map in) throws Exception {
        String token = in.get("token").toString();
        String detail = (String) in.get("detail");
        String title = in.get("title").toString();
        String endTimeStr = (String) in.get("endTime");
        Date endTimeDate = (Date) in.get("endTimeDate");
        String pointStr = (String) in.get("point");
        String teamId = (String) in.get("teamId");

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Team team = null;
        if (teamId != null) {
            team = iCommonBusinessService.getTeamById(teamId);
        }

        Date strtodate = null;

        if (endTimeStr != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            ParsePosition pos = new ParsePosition(0);
            strtodate = formatter.parse(endTimeStr, pos);
        } else {
            if (endTimeDate != null) {
                strtodate = endTimeDate;
            }
        }

        Double point = null;
        try {
            point = Double.parseDouble(pointStr);
        } catch (Exception ex) {
            throw new Exception("20002");
        }

        Task task = new Task();
        task.setCreateTime(new Date());
        task.setCreateUserId(userInfo.getUserId());
        task.setDetail(detail);
        task.setEndTime(strtodate);
        task.setPoint(point);
        task.setTaskId(GogoTools.UUID().toString());
        task.setTitle(title);
        task.setStatus(GogoStatus.GRABBING.toString());
        if (team != null) {
            task.setTeamId(team.getTeamId());
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

        Map qIn = new HashMap();
        //读取我所在的团队
        qIn.put("userId", userInfo.getUserId());
        qIn.put("status", GogoStatus.ACTIVE.toString());
        ArrayList<TeamUser> teamUsers = iTeamService.listTeamUser(qIn);
        if (teamUsers.size() == 0) {
            //我没有加入任何团队
            throw new Exception("20005");
        }
        ArrayList teamList = new ArrayList();
        for (int i = 0; i < teamUsers.size(); i++) {
            teamList.add(teamUsers.get(i).getTeamId());
        }
        qIn.put("teamList", teamList);
        ArrayList<Task> tasks = iTaskService.listBiddingTasks(qIn);
        Map out = new HashMap();
        out.put("tasks", tasks);
        return out;
    }

    @Override
    public Map getTaskByTaskId(Map in) throws Exception {
        String token = in.get("token").toString();
        String taskId = in.get("taskId").toString();

        /**
         * 用户读取任务详情的要求
         * 1、用户必须是已注册且登录的状态
         * 2、检查任务是否属于某个团队
         * 3、如果是团队任务，则当前用户必须是该团队成员才可访问
         * 4、非团队任务，则可直接访问
         */
        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        //读取任务
        Task task = iTaskService.getTaskByTaskId(taskId);
        if (task == null) {
            //没有找到任务
            throw new Exception("10016");
        }

        if (task.getTeamId() == null) {
            //非团队任务
        } else {
            //团队任务，只有团队成员可见
            //读取用户的团队
            Map qIn = new HashMap();
            qIn.put("userId", userInfo.getUserId());
            qIn.put("status", GogoStatus.ACTIVE.toString());
            qIn.put("teamId", task.getTeamId());
            ArrayList<TeamUser> teamUsers = iTeamService.listTeamUser(qIn);
            if (teamUsers.size() == 0) {
                //不是当前团队成员
                throw new Exception("10017");
            }
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
        Integer totalTaskComplete = iTaskCompleteService.totalTaskComplete(task.getTaskId());
        out.put("totalTaskComplete", totalTaskComplete);

        Integer totalUnreadComplete = iTaskCompleteService.totalCompleteUnread(task.getTaskId(), userInfo.getUserId());
        out.put("totalUnreadTaskComplete", totalUnreadComplete);

        //终止日志总数，未阅读数
        Map qIn=new HashMap();
        qIn.put("taskId", taskId);
        qIn.put("partyBId", userInfo.getUserId());
        Integer totalTaskStopUnread=iTaskStopService.totalTaskStopUnread(qIn);
        out.put("totalTaskStopUnread", totalTaskStopUnread);
        Integer totalTaskStop=iTaskStopService.totalTaskStop(taskId);
        out.put("totalTaskStop", totalTaskStop);

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
        String status = (String) in.get("status");

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        qIn.put("status", status);
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

            //

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
         * 任务必须是GRABBING状态
         */
        if (!task.getStatus().equals(GogoStatus.GRABBING.toString())) {
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

    /**
     * 读取我的任务列表，包括详情
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listMyTasksDetail(Map in) throws Exception {
        /**
         * 1、进行中的任务，status=progress
         * 2、我是乙方，partyBId
         * 3、我是甲方，createUserId
         */
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());

        /**
         * 缓冲读取数据列表
         * 读取前一页和后一页，共3页的数据
         */
        Integer offset = 0;
        if (pageIndex > 1) {
            offset = (pageIndex - 2) * pageSize;
        } else {
            offset = (pageIndex - 1) * pageSize;
        }
        Integer size = pageSize * 3;

        qIn.put("offset", offset);
        qIn.put("size", size);

        ArrayList<Task> tasks = iTaskService.listMyTasksDetail(qIn);

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

        //task总数
        qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        Integer totalTasks = iTaskService.totalUserTask(qIn);
        out.put("totalTasks", totalTasks);

        //task总页数
        Integer totalPage = totalTasks / pageSize;
        Integer modPage = totalTasks % pageSize;
        if (modPage > 0) {
            totalPage++;
        }
        out.put("totalPage", totalPage);

        return out;
    }

    /**
     * 修改任务
     *
     * @param in
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateTask(Map in) throws Exception {
        String token = in.get("token").toString();
        String detail = (String) in.get("detail");
        String title = in.get("title").toString();
        String endTimeStr = (String) in.get("endTime");
        Date endTimeDate = (Date) in.get("endTimeDate");
        String pointStr = (String) in.get("point");
        String teamId = (String) in.get("teamId");
        String taskId = (String) in.get("taskId");

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Task task = iTaskService.getTaskByTaskId(taskId);

        if (task == null) {
            throw new Exception("10016");
        }

        if (!task.getStatus().equals(GogoStatus.GRABBING.toString())) {
            // 任务不是等待匹配状态，不能修改
            throw new Exception("20004");
        }

        if (!task.getCreateUserId().equals(userInfo.getUserId())) {
            // 不能修改不是自己创建的任务
            throw new Exception("20003");
        }

        //如果指定了团队Id，检查团队是否存在
        Team team = null;
        if (teamId != null) {
            team = iCommonBusinessService.getTeamById(teamId);
        }

        Date strtodate = null;

        if (endTimeStr != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            ParsePosition pos = new ParsePosition(0);
            strtodate = formatter.parse(endTimeStr, pos);
        } else {
            if (endTimeDate != null) {
                strtodate = endTimeDate;
            }
        }

        Double point = null;
        try {
            point = Double.parseDouble(pointStr);
        } catch (Exception ex) {
            throw new Exception("20002");
        }

        task.setDetail(detail);
        task.setEndTime(strtodate);
        task.setPoint(point);
        task.setTitle(title);
        if (team != null) {
            task.setTeamId(team.getTeamId());
        }

        /**
         * 保存前先检查是否重复
         */
        if (iCommonBusinessService.isDuplicateTask(task)) {
            //重复了
            throw new Exception("10013");
        }

        iTaskService.updateTask(task);
    }

    @Override
    public Map listTaskGrabbingTeam(Map in) throws Exception {
        /**
         * 1、查询所有团队等待匹配的任务
         * 2、status==biding
         * 3、teamId 属于我的team
         */
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        //读取我的团队集合
        Map qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        ArrayList<TeamUser> teamUsers = iTeamService.listTeamUser(qIn);
        if (teamUsers.size() == 0) {
            throw new Exception("20005");
        }
        ArrayList teamList = new ArrayList();
        for (int i = 0; i < teamUsers.size(); i++) {
            teamList.add(teamUsers.get(i).getTeamId());
        }

        qIn = new HashMap();
        qIn.put("teamList", teamList);
        ArrayList<Task> tasks = iTaskService.listTaskGrabbingTeam(qIn);


        Map out = new HashMap();
        out.put("tasks", tasks);
        return out;
    }

    @Override
    public Map listMyPartyATasksDetail(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("partyAId", userInfo.getUserId());
        Integer offset = (pageIndex - 1) * pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<Task> tasks = iTaskService.listMyTasksDetailPartyAOrB(qIn);

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

        //task总数
        qIn = new HashMap();
        qIn.put("partyAId", userInfo.getUserId());
        Integer totalTasks = iTaskService.totalMyTasksPartyAOrB(qIn);
        out.put("totalTasks", totalTasks);

        //task总页数
        Integer totalPage = totalTasks / pageSize;
        Integer modPage = totalTasks % pageSize;
        if (modPage > 0) {
            totalPage++;
        }
        out.put("totalPage", totalPage);

        return out;
    }

    @Override
    public Map listMyPartyBTasksDetail(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("partyBId", userInfo.getUserId());
        Integer offset = (pageIndex - 1) * pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<Task> tasks = iTaskService.listMyTasksDetailPartyAOrB(qIn);

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
}
