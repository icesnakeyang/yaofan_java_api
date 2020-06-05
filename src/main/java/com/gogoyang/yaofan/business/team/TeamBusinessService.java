package com.gogoyang.yaofan.business.team;

import com.gogoyang.yaofan.meta.task.entity.Task;
import com.gogoyang.yaofan.meta.task.service.ITaskService;
import com.gogoyang.yaofan.meta.team.entity.*;
import com.gogoyang.yaofan.meta.team.service.ITeamService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.utility.GogoRole;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.GogoTools;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TeamBusinessService implements ITeamBusinessService {
    private final ICommonBusinessService iCommonBusinessService;
    private final ITeamService iTeamService;
    private final ITaskService iTaskService;

    public TeamBusinessService(ICommonBusinessService iCommonBusinessService,
                               ITeamService iTeamService,
                               ITaskService iTaskService) {
        this.iCommonBusinessService = iCommonBusinessService;
        this.iTeamService = iTeamService;
        this.iTaskService = iTaskService;
    }

    /**
     * 创建一个团队
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map createTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        String name = in.get("name").toString();
        String description = in.get("description").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("teamName", name);
        Team team = iTeamService.getTeam(qIn);

        if (team != null) {
            //团队名称重复
            throw new Exception("10006");
        }

        /**
         * 创建一个团队
         */
        team = new Team();
        team.setCreateTime(new Date());
        team.setDescription(description);
        team.setTeamName(name);
        team.setStatus(GogoStatus.ACTIVE.toString());
        team.setTeamId(GogoTools.UUID().toString());
        team.setCreateUserId(userInfo.getUserId());
        team.setManagerId(userInfo.getUserId());
        team = iTeamService.createTeam(team);

        /**
         * 创建团队的成员连接
         */
        TeamUser teamUser = new TeamUser();
        teamUser.setStatus(GogoStatus.ACTIVE.toString());
        teamUser.setTeamId(team.getTeamId());
        teamUser.setUserId(userInfo.getUserId());
        teamUser.setCreateTime(new Date());
        teamUser.setMemberType(GogoRole.TEAM_MANAGER.toString());
        iTeamService.createTeamUser(teamUser);

        Map out = new HashMap();
        out.put("team", team);

        return out;
    }

    /**
     * 读取我的团队列表
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listMyTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        //读取我的所有团队
        qIn.put("userId", userInfo.getUserId());
        ArrayList<TeamUser> teamUsers = iTeamService.listTeamUser(qIn);

        Map out = new HashMap();
        out.put("teams", teamUsers);
        return out;
    }

    /**
     * 根据团队名称关键字搜索团队
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map searchTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        String name = in.get("name").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("teamName", name);
        qIn.put("status", GogoStatus.ACTIVE.toString());
        ArrayList<Team> teams = iTeamService.listTeam(qIn);

        Map out = new HashMap();
        out.put("teams", teams);
        return out;
    }

    /**
     * 根据teamId读取团队详情
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map getTeamByTeamId(Map in) throws Exception {
        String token = in.get("token").toString();
        String teamId = in.get("teamId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("teamId", teamId);
        Team team = iTeamService.getTeam(qIn);

        Map out = new HashMap();
        out.put("team", team);

        /**
         * 判断当前用户是否为团队成员，或者管理员
         */
        if (team.getManagerId().equals(userInfo.getUserId())) {
            //管理员
            out.put("isManager", true);
        } else {
            //是否是团队成员
            qIn = new HashMap();
            qIn.put("userId", userInfo.getUserId());
            qIn.put("teamId", teamId);
            ArrayList<TeamUser> teamUsers = iTeamService.listTeamUser(qIn);
            if (teamUsers.size() > 0) {
                out.put("isMember", true);
            }
        }

        /**
         * 读取团队成员信息
         */
        qIn = new HashMap();
        qIn.put("teamId", teamId);
        ArrayList<TeamUser> teamUsers = iTeamService.listTeamUser(qIn);
        out.put("teamUsers", teamUsers);

        return out;
    }

    /**
     * 创建团队加入申请日志
     *
     * @param in
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void applyTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        String remark = (String) in.get("remark");
        String teamId = in.get("teamId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        /**
         * 检查该用户是否已经提交过加入该团队的申请，且未被处理
         * 用户不能重复提交，只有处理后才能再次提交。
         * 如果用户已经加入了该团队，也不能再提交申请
         */

        //检查用户是否已经提交过申请，且未处理
        Map qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        qIn.put("teamId", teamId);
        int total = iTeamService.totalApplyTeamUnProcess(qIn);
        if (total > 0) {
            //已经申请过了，等待处理中
            throw new Exception("10007");
        }

        //检查用户是否已经是该团队成员了
        qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        qIn.put("status", GogoStatus.ACTIVE.toString());
        qIn.put("teamId", teamId);
        ArrayList<TeamUser> teamUsers = iTeamService.listTeamUser(qIn);
        if (teamUsers.size() > 0) {
            //已经是该团队成员了，不用申请了
            throw new Exception("10012");
        }

        //创建加入团队申请
        TeamApplyLog teamApplyLog = new TeamApplyLog();
        teamApplyLog.setTeamApplyLogId(GogoTools.UUID().toString());
        teamApplyLog.setApplyRemark(remark);
        teamApplyLog.setTeamId(teamId);
        teamApplyLog.setApplyUserId(userInfo.getUserId());
        teamApplyLog.setCreateTime(new Date());
        teamApplyLog.setStatus(GogoStatus.PENDING.toString());
        iTeamService.createTeamApplyLog(teamApplyLog);
    }

    /**
     * 读取我申请的团队日志
     * 包括已处理和未处理的
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listTeamApplyLogMyApply(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        /**
         * 1、读取自己的用户信息，获得userId
         * 2、查询所有团队申请日志，创建用户id=userId
         */
        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        Integer offset = (pageIndex - 1) * pageSize;
        qIn.put("applyUserId", userInfo.getUserId());
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
//         processUserId status
        ArrayList<TeamApplyLog> applyTeams = iTeamService.listTeamApplyLog(qIn);
        Map out = new HashMap();
        out.put("applyTeams", applyTeams);
        return out;
    }

    /**
     * 读取申请我的团队日志
     * 包括已处理和未处理的
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listTeamApplyLogApplyUser(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        /**
         * 1、读取自己的用户信息，获得userId
         * 2、查询所有团队申请日志，创建用户id=userId
         */
        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("managerId", userInfo.getUserId());

        qIn.put("offset", 0);
        qIn.put("size", 9999);
        ArrayList<Team> teams = iTeamService.listTeam(qIn);
        if (teams.size() == 0) {
            //没有团队，直接退出
            Map out = new HashMap();
            out.put("applyTeams", null);
            return out;
        }
        ArrayList list = new ArrayList();
        for (int i = 0; i < teams.size(); i++) {
            String team = teams.get(i).getTeamId();
            list.add(team);
        }

        Integer offset = (pageIndex - 1) * pageSize;
        if (list.size() > 0) {
            qIn.put("teamList", list);
        }
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
//         processUserId status
        ArrayList<TeamApplyLog> applyTeams = iTeamService.listTeamApplyLog(qIn);
        Map out = new HashMap();
        out.put("applyTeams", applyTeams);
        return out;
    }

    /**
     * 获取团队申请日志详情
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map getTeamApplyLog(Map in) throws Exception {
        String token = in.get("token").toString();
        String teamApplyLogId = in.get("teamApplyLogId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);
        TeamApplyLog teamApplyLog = iTeamService.getTeamApplyLog(teamApplyLogId);

        if (teamApplyLog == null) {
            throw new Exception("10010");
        }
        /**
         * 如果当前用户是管理员，就写入readTime
         */
        boolean checkUser = false;
        if (teamApplyLog.getTeamManagerId().equals(userInfo.getUserId())) {
            //管理员，写入readTime
            Map qIn = new HashMap();
            qIn.put("readTime", new Date());
            qIn.put("teamApplyLogId", teamApplyLogId);
            iTeamService.setTeamApplyLogReadTime(qIn);
            checkUser = true;
        } else {
            if (teamApplyLog.getApplyUserId().equals(userInfo.getUserId())) {
                //申请人，检查是否未阅读处理结果，如果没阅读，就设置阅读时间
                if (teamApplyLog.getProcessTime() != null) {
                    if (teamApplyLog.getProcessReadTime() == null) {
                        //没有阅读
                        Map qIn = new HashMap();
                        qIn.put("readTime", new Date());
                        qIn.put("teamApplyLogId", teamApplyLogId);
                        iTeamService.setTeamApplyLogReadTimeProcess(qIn);
                    }
                }
                checkUser = true;
            }
        }

        /**
         * 如果当前用户既不是管理员，又不是申请人，就退出
         */
        if (!checkUser) {
            throw new Exception("10008");
        }

        Map out = new HashMap();
        out.put("teamApplyLog", teamApplyLog);

        return out;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void rejectApplyTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        String teamApplyLogId = in.get("teamApplyLogId").toString();
        String processRemark = in.get("remark").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);
        TeamApplyLog teamApplyLog = iTeamService.getTeamApplyLog(teamApplyLogId);

        if (teamApplyLog == null) {
            throw new Exception("10010");
        }

        if (!teamApplyLog.getStatus().equals(GogoStatus.PENDING.toString())) {
            throw new Exception("10011");
        }

        /**
         * 必须是该团队的管理员才能拒绝申请
         */
        if (!teamApplyLog.getTeamManagerId().equals(userInfo.getUserId())) {
            //不是管理员
            throw new Exception("10009");
        }

        Map qIn = new HashMap();
        qIn.put("teamApplyLogId", teamApplyLogId);
        qIn.put("status", GogoStatus.REJECT.toString());
        qIn.put("processRemark", processRemark);
        qIn.put("processTime", new Date());
        qIn.put("processUserId", userInfo.getUserId());
        iTeamService.processTeamApplyLog(qIn);
    }

    /**
     * 通过团队加入申请
     *
     * @param in
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void agreeApplyTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        String teamApplyLogId = in.get("teamApplyLogId").toString();
        String processRemark = in.get("remark").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);
        TeamApplyLog teamApplyLog = iTeamService.getTeamApplyLog(teamApplyLogId);

        if (teamApplyLog == null) {
            throw new Exception("10010");
        }

        if (!teamApplyLog.getStatus().equals(GogoStatus.PENDING.toString())) {
            throw new Exception("10011");
        }

        /**
         * 必须是该团队的管理员才能处理
         */
        if (!teamApplyLog.getTeamManagerId().equals(userInfo.getUserId())) {
            //不是管理员
            throw new Exception("10009");
        }

        //保存处理结果
        Map qIn = new HashMap();
        qIn.put("teamApplyLogId", teamApplyLogId);
        qIn.put("status", GogoStatus.AGREE.toString());
        qIn.put("processRemark", processRemark);
        qIn.put("processTime", new Date());
        qIn.put("processUserId", userInfo.getUserId());

        iTeamService.processTeamApplyLog(qIn);

        /**
         * 增加团队成员连接记录
         */
        TeamUser teamUser = new TeamUser();
        teamUser.setStatus(GogoStatus.ACTIVE.toString());
        teamUser.setTeamId(teamApplyLog.getTeamId());
        teamUser.setUserId(teamApplyLog.getApplyUserId());
        teamUser.setMemberType(GogoRole.NORMAL.toString());
        iTeamService.createTeamUser(teamUser);
    }

    /**
     * 修改我的团队信息
     *
     * @param in
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMyTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        String teamId = in.get("teamId").toString();
        String name = (String) in.get("name");
        String description = (String) in.get("description");

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("teamId", teamId);
        Team team = iTeamService.getTeam(qIn);

        if (team == null) {
            throw new Exception("10014");
        }

        if (!team.getManagerId().equals(userInfo.getUserId())) {
            //用户不是该团队管理员
            throw new Exception("10009");
        }

        qIn = new HashMap();
        qIn.put("name", name);
        qIn.put("description", description);
        qIn.put("teamId", teamId);
        iTeamService.updateTeam(qIn);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteMyTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        String teamId = in.get("teamId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Team team = iCommonBusinessService.getTeamById(teamId);

        if (!team.getManagerId().equals(userInfo.getUserId())) {
            //不是管理员，不能删除
            throw new Exception("10009");
        }

        /**
         * 检查团队是否存在任务
         * todo
         */


        /**
         * 删除团队
         * 这里实际上是假删，把团队设置为DELETE状态就行了
         */

        Map qIn = new HashMap();
        qIn.put("teamId", teamId);
        qIn.put("status", GogoStatus.DELETE);
        qIn.put("lastUpdateTime", new Date());
        qIn.put("lastUpdateUserId", userInfo.getUserId());
        iTeamService.setTeamStatus(qIn);
    }

    /**
     * 统计当前用户的团队日志数量
     * 1、统计未读的加入我的团队申请
     * 2、统计未读的已处理的我加入的团队申请
     * 3、统计我加入的团队申请总数
     * 4、统计加入我的团队申请总数
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map totalMyTeamLog(Map in) throws Exception {
        String token = in.get("token").toString();
        /**
         * 统计未读的加入我的团队申请
         * 1、读取当前用户
         * 2、查询当前用户创建并负责的团队
         * 3、如果没有就退出
         * 4、如果有，统计未读的团队申请数量
         */
        Map out = new HashMap();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);
        Map qIn = new HashMap();
        qIn.put("managerId", userInfo.getUserId());
        qIn.put("status", GogoStatus.ACTIVE.toString());
        qIn.put("offset", 0);
        qIn.put("size", 100000);
        ArrayList<Team> teams = iTeamService.listTeam(qIn);

        ArrayList teamList = new ArrayList();
        if (teams.size() == 0) {
            //当前用户没有创建的团队，返回0
            out.put("totalNewApplyMember", 0);
        } else {
            for (int i = 0; i < teams.size(); i++) {
                teamList.add(teams.get(i).getTeamId());
            }
            qIn.put("teamList", teamList);
            Integer total = iTeamService.totalTeamApplyLogUnRead(qIn);
            out.put("totalNewApplyMember", total);
        }

        /**
         * 统计未读的已处理的我加入的团队申请
         */
        qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        Integer total = iTeamService.totalTeamApplyLogUnReadProcess(qIn);
        out.put("totalUnreadProcess", total);

        /**
         * 统计我加入的团队申请总数
         */
        qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        Integer totalMyApply = iTeamService.totalTeamApplyLogMyApply(qIn);
        out.put("totalTeamApplyLogMyApply", totalMyApply);

        /**
         * 统计加入我的团队申请总数
         */
        if (teamList.size() > 0) {
            //有自己的团队
            qIn = new HashMap();
            qIn.put("teamList", teamList);
            Integer totalTeamApplyLogMyTeam = iTeamService.totalTeamApplyLogMyTeam(qIn);
            out.put("totalTeamApplyLogMyTeam", totalTeamApplyLogMyTeam);
        }

        return out;
    }

    /**
     * 取消加入团队的申请
     *
     * @param in
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelTeamApplyLog(Map in) throws Exception {
        String token = in.get("token").toString();
        String teamApplyLogId = in.get("teamApplyLogId").toString();

        /**
         * 1、读取我的信息
         * 2、读取申请日志
         * 3、日志状态必须为pending
         * 4、检查是否我创建的日志
         * 5、保存日志状态为cancel
         */
        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        TeamApplyLog teamApplyLog = iTeamService.getTeamApplyLog(teamApplyLogId);

        if (!teamApplyLog.getStatus().equals(GogoStatus.PENDING.toString())) {
            //该日志不是等待处理状态
            throw new Exception("20006");
        }

        if (!teamApplyLog.getApplyUserId().equals(userInfo.getUserId())) {
            //当前用户是日志创建者，不能取消
            throw new Exception("20007");
        }
        Map qIn = new HashMap();
        qIn.put("status", GogoStatus.CANCEL.toString());
        qIn.put("teamApplyLogId", teamApplyLogId);
        iTeamService.cancelTeamApplyLog(qIn);
    }

    /**
     * 用户提交退团申请
     *
     * @param in
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void quitTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        String teamId = in.get("teamId").toString();
        String remark = in.get("remark").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Team team = iCommonBusinessService.getTeamById(teamId);

        //首先检查用户是否为该团队成员

        //检查用户是否有未完成的任务

        //其他退团条件检查

        //创建退团日志
        TeamQuitLog teamQuitLog = new TeamQuitLog();
        teamQuitLog.setCreateTime(new Date());
        teamQuitLog.setRemark(remark);
        teamQuitLog.setStatus(GogoStatus.PENDING.toString());
        teamQuitLog.setTeamId(teamId);
        teamQuitLog.setTeamQuitLogId(GogoTools.UUID().toString());
        teamQuitLog.setUserId(userInfo.getUserId());

        iTeamService.createTeamQuitLog(teamQuitLog);
    }

    /**
     * 查询我发起的退团申请列表
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listTeamQuitLogApply(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        Integer offset = (pageIndex - 1) * pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        qIn.put("userId", userInfo.getUserId());
        ArrayList<TeamQuitLog> teamQuitLogs = iTeamService.listTeamQuitLog(qIn);

        Map out = new HashMap();
        out.put("teamQuitLogs", teamQuitLogs);

        return out;
    }

    /**
     * 查询我收到的退团申请列表
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listTeamQuitLogProcess(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        Integer offset = (pageIndex - 1) * pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        qIn.put("managerId", userInfo.getUserId());
        ArrayList<TeamQuitLog> teamQuitLogs = iTeamService.listTeamQuitLog(qIn);

        Map out = new HashMap();
        out.put("teamQuitLogs", teamQuitLogs);

        return out;
    }

    @Override
    public Map getTeamQuitLog(Map in) throws Exception {
        String token = in.get("token").toString();
        String teamQuitLogId = in.get("teamQuitLogId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("teamQuitLogId", teamQuitLogId);
        TeamQuitLog teamQuitLog = iTeamService.getTeamQuitLog(qIn);

        Map out = new HashMap();
        out.put("teamQuitLog", teamQuitLog);

        /**
         * 检查当前用户身份，申请人，处理人
         */
        if (teamQuitLog.getUserId().equals(userInfo.getUserId())) {
            //当前用户是申请人
            out.put("isApply", true);
        } else {
            if (teamQuitLog.getProcessUserId() != null) {
                //已处理
                if (teamQuitLog.getProcessUserId().equals(userInfo.getUserId())) {
                    //当前用户是处理人
                    out.put("isProcess", true);
                }
            } else {
                //未处理，查询团队负责人
                qIn = new HashMap();
                qIn.put("teamId", teamQuitLog.getTeamId());
                Team team = iTeamService.getTeam(qIn);
                if (team != null) {
                    if (team.getManagerId().equals(userInfo.getUserId())) {
                        //当前用户是团队管理员
                        out.put("isProcess", true);
                    }
                }
            }
        }

        return out;
    }

    /**
     * 取消退出团队申请
     *
     * @param in
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelTeamQuitLog(Map in) throws Exception {
        String token = in.get("token").toString();
        String teamQuitLogId = in.get("teamQuitLogId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("teamQuitLogId", teamQuitLogId);
        TeamQuitLog teamQuitLog = iTeamService.getTeamQuitLog(qIn);

        if (!teamQuitLog.getUserId().equals(userInfo.getUserId())) {
            //不是本人的申请，不能取消
            throw new Exception("20007");
        }

        qIn = new HashMap();
        qIn.put("teamQuitLogId", teamQuitLogId);
        qIn.put("status", GogoStatus.CANCEL);
        qIn.put("processTime", new Date());
        qIn.put("processUserId", userInfo.getUserId());
        iTeamService.processTeamQuitLog(qIn);
    }

    @Override
    public Map listMyHistoryTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("managerId", userInfo.getUserId());
        qIn.put("teamStatusNot", GogoStatus.ACTIVE.toString());
        qIn.put("userId", userInfo.getUserId());
        ArrayList<TeamUser> teamUsers = iTeamService.listTeamUser(qIn);

        Map out = new HashMap();
        out.put("historyTeams", teamUsers);

        return out;
    }

    @Override
    public void rollbackTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        String teamId = in.get("teamId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Team team = iCommonBusinessService.getTeamById(teamId);

        if (team.getStatus().equals(GogoStatus.ACTIVE.toString())) {
            //团队当前是正常状态
            throw new Exception("20025");
        }

        if (!userInfo.getUserId().equals(team.getManagerId())) {
            //不是团队管理员，不能进行恢复操作
            throw new Exception("10009");
        }

        Map qIn = new HashMap();
        qIn.put("status", GogoStatus.ACTIVE);
        qIn.put("teamId", teamId);
        qIn.put("lastUpdateTime", new Date());
        qIn.put("lastUpdateUserId", userInfo.getUserId());
        iTeamService.setTeamStatus(qIn);
    }

    @Override
    public Map listMyTeamMember(Map in) throws Exception {
        String token = in.get("token").toString();
        String teamId = in.get("teamId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Team team = iCommonBusinessService.getTeamById(teamId);

        Map qIn = new HashMap();
        qIn.put("teamId", teamId);
        qIn.put("status", GogoStatus.ACTIVE);
        ArrayList<TeamUser> teamUsers = iTeamService.listTeamUser(qIn);

        Map out = new HashMap();
        out.put("teamUsers", teamUsers);

        return out;
    }

    @Override
    public Map getMemberProfile(Map in) throws Exception {
        String token = in.get("token").toString();
        String userId = in.get("userId").toString();
        String teamId = in.get("teamId").toString();

        UserInfo loginUser = iCommonBusinessService.getUserByToken(token);

        UserInfo checkUser = iCommonBusinessService.getUserByUserId(userId);

        Map out = new HashMap();

        /**
         * todo
         * 装载checkUser的详情
         */

        out.put("userInfo", checkUser);

        /**
         * user的团队角色
         */
        Map qIn = new HashMap();
        qIn.put("teamId", teamId);
        qIn.put("userId", checkUser.getUserId());
        ArrayList<TeamUser> teamUsers = iTeamService.listTeamUser(qIn);
        if (teamUsers.size() == 0) {
            throw new Exception("10017");
        }
        out.put("memberType", teamUsers.get(0).getMemberType());

        return out;


    }

    @Override
    public void ResignMember(Map in) throws Exception {
        String token = in.get("token").toString();
        String userId = in.get("userId").toString();
        String teamId = in.get("teamId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        UserInfo resignUser = iCommonBusinessService.getUserByUserId(userId);

        /**
         * 只有团队管理员才能解除团队成员
         */
        Map qIn = new HashMap();
        qIn.put("teamId", teamId);
        Team team = iTeamService.getTeam(qIn);
        if (team == null) {
            throw new Exception("10014");
        }
        if (!team.getManagerId().equals(userInfo.getUserId())) {
            throw new Exception("10009");
        }

        /**
         * 检查任务
         */
        qIn = new HashMap();
        qIn.put("userId", resignUser.getUserId());
        qIn.put("status", GogoStatus.PROGRESS.toString());
        ArrayList<Task> tasks = iTaskService.listMyTasks(qIn);

//        if(tasks.size()>0){
//            //用户还存在正在进行中的任务，设置状态为异常
        //这里暂时不需要处理任务。
//            for(int i=0;i<tasks.size();i++){
//                Task task=tasks.get(i);
//                Task taskUpdate=new Task();
//                taskUpdate.setTaskId(task.getTaskId());
//                taskUpdate.setStatus(GogoStatus.EXCEPTION.toString());
//                iTaskService.updateTaskStatus(taskUpdate);
//            }
//        }

        /**
         * 把当前用户在当前团队里设置为RESIGN状态
         */
        qIn = new HashMap();
        qIn.put("userId", userId);
        qIn.put("teamId", teamId);
        qIn.put("status", GogoStatus.ACTIVE.toString());
        ArrayList<TeamUser> teamUsers = iTeamService.listTeamUser(qIn);
        if (teamUsers.size() == 1) {
            qIn = new HashMap();
            qIn.put("ids", teamUsers.get(0).getIds());
            qIn.put("status", GogoStatus.RESIGN);
            iTeamService.updateTeamUser(qIn);
        } else {
            //用户重复
            throw new Exception("20026");
        }

    }

    @Override
    public void setObserver(Map in) throws Exception {
        String token = in.get("token").toString();
        String userId = in.get("userId").toString();
        String teamId = in.get("teamId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        UserInfo observer = iCommonBusinessService.getUserByUserId(userId);

        Team team = iCommonBusinessService.getTeamById(teamId);

        /**
         * 1、检查登录用户是否为团队管理员
         * 2、检查观察者是否为团队成员
         * 3、修改观察者的团队角色memberType=TEAM_OBSERVER
         */

        if (!team.getManagerId().equals(userInfo.getUserId())) {
            //你不是团队管理员
            throw new Exception("10009");
        }
        Map qIn = new HashMap();
        qIn.put("teamId", teamId);
        qIn.put("userId", observer.getUserId());
        ArrayList<TeamUser> teamUsers = iTeamService.listTeamUser(qIn);
        if (teamUsers.size() == 0) {
            throw new Exception("20027");
        }
        qIn = new HashMap();
        qIn.put("ids", teamUsers.get(0).getIds());
        qIn.put("memberType", GogoRole.TEAM_OBSERVER);
        iTeamService.updateTeamUser(qIn);
    }

    @Override
    public void relieveObserver(Map in) throws Exception {
        String token = in.get("token").toString();
        String userId = in.get("userId").toString();
        String teamId = in.get("teamId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        UserInfo observer = iCommonBusinessService.getUserByUserId(userId);

        Team team = iCommonBusinessService.getTeamById(teamId);

        /**
         * 1、检查登录用户是否为团队管理员
         * 2、检查观察者是否为团队成员
         * 3、修改观察者的团队角色memberType=TEAM_OBSERVER
         */

        if (!team.getManagerId().equals(userInfo.getUserId())) {
            //你不是团队管理员
            throw new Exception("10009");
        }
        Map qIn = new HashMap();
        qIn.put("teamId", teamId);
        qIn.put("userId", observer.getUserId());
        ArrayList<TeamUser> teamUsers = iTeamService.listTeamUser(qIn);
        if (teamUsers.size() == 0) {
            throw new Exception("20027");
        }
        qIn = new HashMap();
        qIn.put("ids", teamUsers.get(0).getIds());
        qIn.put("memberType", GogoRole.NORMAL);
        iTeamService.updateTeamUser(qIn);
    }
}
