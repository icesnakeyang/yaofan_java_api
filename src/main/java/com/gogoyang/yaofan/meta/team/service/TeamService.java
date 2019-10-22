package com.gogoyang.yaofan.meta.team.service;

import com.gogoyang.yaofan.meta.team.dao.TeamDao;
import com.gogoyang.yaofan.meta.team.entity.MyTeam;
import com.gogoyang.yaofan.meta.team.entity.MyTeamView;
import com.gogoyang.yaofan.meta.team.entity.Team;
import org.springframework.beans.factory.annotation.Autowired;
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
}
