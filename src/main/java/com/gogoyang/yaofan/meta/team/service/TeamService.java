package com.gogoyang.yaofan.meta.team.service;

import com.gogoyang.yaofan.meta.team.dao.TeamDao;
import com.gogoyang.yaofan.meta.team.entity.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class TeamService implements ITeamService {
    private final TeamDao teamDao;

    public TeamService(TeamDao teamDao) {
        this.teamDao = teamDao;
    }

    @Override
    public Team createTeam(Team team) throws Exception {
        teamDao.createTeam(team);
        return team;
    }

    @Override
    public Team getTeamByName(String name) throws Exception {
        Team team = teamDao.getTeamByName(name);
        return team;
    }

    @Override
    public MyTeam createMyTeam(MyTeam myTeam) throws Exception {
        teamDao.createMyTeam(myTeam);
        return myTeam;
    }

    @Override
    public ArrayList<MyTeamView> listTeam(String userId, String status) throws Exception {
        Map qIn = new HashMap();
        qIn.put("userId", userId);
        qIn.put("status", status);
        ArrayList<MyTeamView> myTeamViewArrayList = teamDao.listTeam(qIn);
        return myTeamViewArrayList;
    }

    /**
     * 根据团队名称关键字搜索团队
     *
     * @param qIn
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<Team> searchTeam(Map qIn) throws Exception {
        ArrayList<Team> teams = teamDao.searchTeam(qIn);
        return teams;
    }

    @Override
    public TeamView getTeamByTeamId(String teamId) throws Exception {
        TeamView team = teamDao.getTeamByTeamId(teamId);
        return team;
    }

    @Override
    public void createApplyTeam(ApplyTeam applyTeam) throws Exception {
        teamDao.createApplyTeam(applyTeam);
    }

    @Override
    public ArrayList<ApplyTeam> listApplyTeam(Map qIn) throws Exception {
        ArrayList<ApplyTeam> applyTeams = teamDao.listApplyTeam(qIn);
        return applyTeams;
    }
}
