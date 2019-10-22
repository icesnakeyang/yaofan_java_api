package com.gogoyang.yaofan.meta.team.service;

import com.gogoyang.yaofan.meta.team.entity.MyTeam;
import com.gogoyang.yaofan.meta.team.entity.MyTeamView;
import com.gogoyang.yaofan.meta.team.entity.Team;

import java.util.ArrayList;
import java.util.Map;

public interface ITeamService {
    Team createTeam(Team team) throws Exception;

    Team getTeamByName(String name) throws Exception;

    MyTeam createMyTeam(MyTeam myTeam) throws Exception;

    ArrayList<MyTeamView> listTeam(String userId, String status) throws Exception;
}
