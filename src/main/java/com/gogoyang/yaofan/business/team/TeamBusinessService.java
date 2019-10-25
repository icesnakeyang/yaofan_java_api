package com.gogoyang.yaofan.business.team;

import com.gogoyang.yaofan.meta.team.entity.*;
import com.gogoyang.yaofan.meta.team.service.ITeamService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
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

        ApplyTeam applyTeam = new ApplyTeam();
        applyTeam.setApplyRemark(remark);
        applyTeam.setApplyTeamId(teamId);
        applyTeam.setApplyUserId(userInfo.getUserId());
        applyTeam.setCreateTime(new Date());

        iTeamService.createApplyTeam(applyTeam);
    }
}
