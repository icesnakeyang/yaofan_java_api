package com.gogoyang.yaofan.business.team;

import com.gogoyang.yaofan.meta.team.entity.*;
import com.gogoyang.yaofan.meta.team.service.ITeamService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.utility.GogoActType;
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

    @Override
    public Map listTeam(Map in) throws Exception {
        String token = in.get("token").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        ArrayList myTeams = iTeamService.listTeam(userInfo.getUserId(), GogoStatus.ACTIVE.toString());

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

        ArrayList<MyTeamView> myTeamViews = iTeamService.listTeam(userInfo.getUserId(), GogoStatus.ACTIVE.toString());
        if (myTeamViews.size() > 0) {
            for (int i = 0; i < myTeamViews.size(); i++) {
                if (teamId.equals(myTeamViews.get(i).getTeamId())) {
                    //已经是该团队成员了，不用申请了
                    throw new Exception("10012");
                }
            }
        }

        ApplyTeam applyTeam = new ApplyTeam();
        applyTeam.setApplyTeamLogId(GogoTools.UUID().toString());
        applyTeam.setApplyRemark(remark);
        applyTeam.setApplyTeamId(teamId);
        applyTeam.setApplyUserId(userInfo.getUserId());
        applyTeam.setCreateTime(new Date());

        iTeamService.createApplyTeam(applyTeam);
    }

    @Override
    public Map listApplyTeam(Map in) throws Exception {
        String token = in.get("token").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        ArrayList<ApplyTeam> applyTeams = iTeamService.listApplyTeam(qIn);
        Map out = new HashMap();
        out.put("applyTeams", applyTeams);
        return out;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map getApplyTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        String applyId = in.get("applyId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);
        ApplyTeamView applyTeamView = iTeamService.getApplyTeam(applyId);

        if (applyTeamView == null) {
            throw new Exception("10010");
        }
        /**
         * 如果当前用户是管理员，就写入readTime
         */
        boolean checkUser = false;
        if (applyTeamView.getManagerId().equals(userInfo.getUserId())) {
            //管理员，写入readTime
            applyTeamView.setReadTime(new Date());
            iTeamService.setApplyTeamReadTime(applyTeamView);
            checkUser = true;
        } else {
            if (applyTeamView.getApplyUserId().equals(userInfo.getUserId())) {
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
        out.put("applyTeamView", applyTeamView);

        return out;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void rejectApplyTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        String applyId = in.get("applyId").toString();
        String processRemark = in.get("remark").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);
        ApplyTeamView applyTeamView = iTeamService.getApplyTeam(applyId);

        if (applyTeamView == null) {
            throw new Exception("10010");
        }

        if (applyTeamView.getProcessResult() != null) {
            throw new Exception("10011");
        }

        /**
         * 必须是该团队的管理员才能拒绝申请
         */
        if (!applyTeamView.getManagerId().equals(userInfo.getUserId())) {
            //不是管理员
            throw new Exception("10009");
        }

        ApplyTeam applyTeam = new ApplyTeam();
        applyTeam.setApplyTeamLogId(applyTeamView.getApplyTeamLogId());
        applyTeam.setProcessRemark(processRemark);
        applyTeam.setProcessResult(GogoStatus.REJECT.toString());
        applyTeam.setProcessTime(new Date());
        applyTeam.setProcessUserId(userInfo.getUserId());
        iTeamService.processApplyTeam(applyTeam);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void agreeApplyTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        String applyId = in.get("applyId").toString();
        String processRemark = in.get("remark").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);
        ApplyTeamView applyTeamView = iTeamService.getApplyTeam(applyId);

        if (applyTeamView == null) {
            throw new Exception("10010");
        }

        if (applyTeamView.getProcessResult() != null) {
            throw new Exception("10011");
        }

        /**
         * 必须是该团队的管理员才能处理
         */
        if (!applyTeamView.getManagerId().equals(userInfo.getUserId())) {
            //不是管理员
            throw new Exception("10009");
        }

        ApplyTeam applyTeam = new ApplyTeam();
        applyTeam.setApplyTeamLogId(applyTeamView.getApplyTeamLogId());
        applyTeam.setProcessRemark(processRemark);
        applyTeam.setProcessResult(GogoStatus.AGREE.toString());
        applyTeam.setProcessTime(new Date());
        applyTeam.setProcessUserId(userInfo.getUserId());
        iTeamService.processApplyTeam(applyTeam);

        /**
         * 增加my_team记录
         */
        MyTeam myTeam = new MyTeam();
        myTeam.setStatus(GogoStatus.ACTIVE.toString());
        myTeam.setTeamId(applyTeamView.getApplyTeamId());
        myTeam.setUserId(applyTeamView.getApplyUserId());
        iTeamService.createMyTeam(myTeam);
    }
}
