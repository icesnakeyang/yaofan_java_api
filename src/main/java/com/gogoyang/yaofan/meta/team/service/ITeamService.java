package com.gogoyang.yaofan.meta.team.service;

import com.gogoyang.yaofan.meta.team.entity.MyTeam;
import com.gogoyang.yaofan.meta.team.entity.MyTeamView;
import com.gogoyang.yaofan.meta.team.entity.Team;
import com.gogoyang.yaofan.meta.team.entity.TeamView;

import java.util.ArrayList;
import java.util.Map;

public interface ITeamService {
    Team createTeam(Team team) throws Exception;

    Team getTeamByName(String name) throws Exception;

    MyTeam createMyTeam(MyTeam myTeam) throws Exception;

    ArrayList<MyTeamView> listTeam(String userId, String status) throws Exception;

    /**
     * 根据团队名称关键字搜索团队
     * @param qIn
     * @return
     * @throws Exception
     */
    ArrayList<Team> searchTeam(Map qIn) throws Exception;

    TeamView getTeamByTeamId(String teamId) throws Exception;
}
