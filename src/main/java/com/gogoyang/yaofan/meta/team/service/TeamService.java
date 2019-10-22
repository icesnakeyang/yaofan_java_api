package com.gogoyang.yaofan.meta.team.service;

import com.gogoyang.yaofan.meta.team.dao.TeamDao;
import com.gogoyang.yaofan.meta.team.entity.MyTeam;
import com.gogoyang.yaofan.meta.team.entity.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
