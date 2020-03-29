package com.gogoyang.yaofan.business.team;

import com.gogoyang.yaofan.meta.team.entity.*;
import com.gogoyang.yaofan.meta.team.service.ITeamService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.utility.GogoActType;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.GogoTools;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.omg.CORBA.INTERNAL;
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

    public TeamBusinessService(ICommonBusinessService iCommonBusinessService,
                               ITeamService iTeamService) {
        this.iCommonBusinessService = iCommonBusinessService;
        this.iTeamService = iTeamService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map createTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        String name = in.get("name").toString();
        String description = in.get("description").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Team team = iTeamService.getTeamByName(name);


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
        team.setName(name);
        team.setStatus(GogoStatus.ACTIVE.toString());
        team.setTeamId(GogoTools.UUID().toString());
        team.setCreateUserId(userInfo.getUserId());
        team.setManagerId(userInfo.getUserId());
        team = iTeamService.createTeam(team);

        /**
         * 创建我的团队记录
         */
        MyTeam myTeam = new MyTeam();
        myTeam.setStatus(GogoStatus.ACTIVE.toString());
        myTeam.setTeamId(team.getTeamId());
        myTeam.setUserId(userInfo.getUserId());
        iTeamService.createMyTeam(myTeam);

        Map out = new HashMap();
        out.put("team", team);

        return out;
    }

    /**
     * 读取团队列表
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
        Integer offset = (pageIndex - 1) * pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        qIn.put("userId", userInfo.getUserId());
        qIn.put("status", GogoStatus.ACTIVE.toString());

        ArrayList myTeams = iTeamService.listTeam(qIn);

        Map out = new HashMap();
        out.put("teams", myTeams);
        return out;
    }

    @Override
    public Map searchTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        String name = in.get("name").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("nameKey", name);
        qIn.put("teamStatus", GogoStatus.ACTIVE.toString());
        qIn.put("currentUserId", userInfo.getUserId());
        ArrayList<Team> teams = iTeamService.searchTeam(qIn);

        Map out = new HashMap();
        out.put("teams", teams);
        return out;
    }

    @Override
    public Map getTeamByTeamId(Map in) throws Exception {
        String token = in.get("token").toString();
        String teamId = in.get("teamId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        TeamView team = iTeamService.getTeamByTeamId(teamId);

        Map out = new HashMap();
        out.put("team", team);

        return out;
    }

    /**
     * 创建团队加入申请日志
     * @param in
     * @throws Exception
     */
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
        Map qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        qIn.put("teamId", teamId);
        int total = iTeamService.totalApplyTeamUnProcess(qIn);
        if (total > 0) {
            //已经申请过了，等待处理中
            throw new Exception("10007");
        }

        qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        qIn.put("status", GogoStatus.ACTIVE.toString());
        ArrayList<MyTeamView> myTeamViews = iTeamService.listTeam(qIn);
        if (myTeamViews.size() > 0) {
            for (int i = 0; i < myTeamViews.size(); i++) {
                if (teamId.equals(myTeamViews.get(i).getTeamId())) {
                    //已经是该团队成员了，不用申请了
                    throw new Exception("10012");
                }
            }
        }

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
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listTeamApplyLogMyApply(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");

        /**
         * 1、读取自己的用户信息，获得userId
         * 2、查询所有团队申请日志，创建用户id=userId
         */
        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        Integer offset=(pageIndex-1)*pageSize;
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
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listTeamApplyLogApplyUser(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");

        /**
         * 1、读取自己的用户信息，获得userId
         * 2、查询所有团队申请日志，创建用户id=userId
         */
        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        ArrayList<TeamView> teams=iTeamService.listMyCreateTeam(qIn);
        ArrayList list=new ArrayList();
        for(int i=0;i<teams.size();i++){
            String team=teams.get(i).getTeamId();
            list.add(team);
        }

        Integer offset=(pageIndex-1)*pageSize;
        qIn.put("teamList", list);
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
        TeamApplyView teamApplyView = iTeamService.getTeamApplyLog(teamApplyLogId);

        if (teamApplyView == null) {
            throw new Exception("10010");
        }
        /**
         * 如果当前用户是管理员，就写入readTime
         */
        boolean checkUser = false;
        if (teamApplyView.getTeamManagerId().equals(userInfo.getUserId())) {
            //管理员，写入readTime
            teamApplyView.setReadTime(new Date());
            Map qIn=new HashMap();
            iTeamService.setTeamApplyLogReadTime(qIn);
            checkUser = true;
        } else {
            if (teamApplyView.getApplyUserId().equals(userInfo.getUserId())) {
                //申请人
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
        out.put("applyTeamView", teamApplyView);

        return out;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void rejectApplyTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        String teamApplyLogId = in.get("teamApplyLogId").toString();
        String processRemark = in.get("remark").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);
        TeamApplyView teamApplyView = iTeamService.getTeamApplyLog(teamApplyLogId);

        if (teamApplyView == null) {
            throw new Exception("10010");
        }

        if (!teamApplyView.getStatus().equals(GogoStatus.PENDING.toString())) {
            throw new Exception("10011");
        }

        /**
         * 必须是该团队的管理员才能拒绝申请
         */
        if (!teamApplyView.getTeamManagerId().equals(userInfo.getUserId())) {
            //不是管理员
            throw new Exception("10009");
        }

        Map qIn=new HashMap();
        qIn.put("teamApplyLogId", teamApplyLogId);
        qIn.put("status", GogoStatus.REJECT.toString());
        qIn.put("processRemark", processRemark);
        qIn.put("processTime", new Date());
        qIn.put("processUserId", userInfo.getUserId());
        iTeamService.processTeamApplyLog(qIn);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void agreeApplyTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        String teamApplyLogId = in.get("teamApplyLogId").toString();
        String processRemark = in.get("remark").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);
        TeamApplyView teamApplyView = iTeamService.getTeamApplyLog(teamApplyLogId);

        if (teamApplyView == null) {
            throw new Exception("10010");
        }

        if (teamApplyView.getStatus() != null) {
            throw new Exception("10011");
        }

        /**
         * 必须是该团队的管理员才能处理
         */
        if (!teamApplyView.getTeamManagerId().equals(userInfo.getUserId())) {
            //不是管理员
            throw new Exception("10009");
        }

        TeamApplyLog teamApplyLog = new TeamApplyLog();
        teamApplyLog.setTeamApplyLogId(teamApplyLog.getTeamApplyLogId());
//        applyTeam.setProcessRemark(processRemark);
//        applyTeam.setProcessResult(GogoStatus.AGREE.toString());
//        applyTeam.setProcessTime(new Date());
//        applyTeam.setProcessUserId(userInfo.getUserId());
        Map qIn=new HashMap();

        iTeamService.processTeamApplyLog(qIn);

        /**
         * 增加my_team记录
         */
        MyTeam myTeam = new MyTeam();
        myTeam.setStatus(GogoStatus.ACTIVE.toString());
        myTeam.setTeamId(teamApplyLog.getTeamApplyLogId());
        myTeam.setUserId(teamApplyLog.getApplyUserId());
        iTeamService.createMyTeam(myTeam);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMyTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        String teamId = in.get("teamId").toString();
        String name = (String) in.get("name");
        String description = (String) in.get("description");

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        TeamView teamView = iTeamService.getTeamByTeamId(teamId);

        if (teamView == null) {
            throw new Exception("10014");
        }

        if (!teamView.getManagerId().equals(userInfo.getUserId())) {
            //用户不是该团队管理员
            throw new Exception("10009");
        }

        Map qIn = new HashMap();
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

        TeamView teamView = iCommonBusinessService.getTeamById(teamId);

        if (!teamView.getManagerId().equals(userInfo.getUserId())) {
            //不是管理员，不能删除
            throw new Exception("10009");
        }

        iTeamService.deleteTeam(teamId);
    }

    /**
     * 统计当前用户创建的团队的新申请加入人数
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map totalNewApplyMember(Map in) throws Exception {
        String token=in.get("token").toString();

        /**
         * 1、读取当前用户
         * 2、查询当前用户创建并负责的团队
         * 3、如果没有就退出
         * 4、如果有，统计未读的团队申请数量
         */
        Map out=new HashMap();

        UserInfo userInfo=iCommonBusinessService.getUserByToken(token);
        Map qIn=new HashMap();
        qIn.put("userId", userInfo.getUserId());
        qIn.put("status",GogoStatus.ACTIVE.toString());
        ArrayList<TeamView> teamViews=iTeamService.listMyCreateTeam(qIn);
        if(teamViews.size()==0){
            //当前用户没有创建的团队，返回0
            out.put("totalNewApplyMember",0);
            return out;
        }
        ArrayList teamList=new ArrayList();
        for(int i=0;i<teamViews.size();i++){
            teamList.add(teamViews.get(i).getTeamId());
        }
        qIn.put("teamList", teamList);
        Integer total=iTeamService.totalTeamApplyLogUnRead(qIn);
        out.put("totalNewApplyMember", total);
        return out;
    }

    /**
     * 取消加入团队的申请
     * @param in
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelTeamApplyLog(Map in) throws Exception {
        String token=in.get("token").toString();
        String teamApplyLogId=in.get("teamApplyLogId").toString();

        /**
         * 1、读取我的信息
         * 2、读取申请日志
         * 3、日志状态必须为pending
         * 4、检查是否我创建的日志
         * 5、保存日志状态为cancel
         */
        UserInfo userInfo=iCommonBusinessService.getUserByToken(token);

        TeamApplyView teamApplyView=iTeamService.getTeamApplyLog(teamApplyLogId);

        if(!teamApplyView.getStatus().equals(GogoStatus.PENDING.toString())){
            //该日志不是等待处理状态
            throw new Exception("20006");
        }

        if(!teamApplyView.getApplyUserId().equals(userInfo.getUserId())){
            //当前用户是日志创建者，不能取消
            throw new Exception("20007");
        }
        Map qIn=new HashMap();
        qIn.put("status", GogoStatus.CANCEL.toString());
        qIn.put("teamApplyLogId", teamApplyLogId);
        iTeamService.cancelTeamApplyLog(qIn);
    }
}
