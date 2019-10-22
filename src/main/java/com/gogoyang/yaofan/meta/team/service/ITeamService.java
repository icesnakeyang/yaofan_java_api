package com.gogoyang.yaofan.meta.team.service;

import com.gogoyang.yaofan.meta.team.entity.MyTeam;
import com.gogoyang.yaofan.meta.team.entity.Team;

public interface ITeamService {
    Team createTeam(Team team) throws Exception;

    Team getTeamByName(String name) throws Exception;

    MyTeam createMyTeam(MyTeam myTeam) throws Exception;
}
