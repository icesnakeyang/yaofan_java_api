package com.gogoyang.yaofan.meta.team.dao;

import com.gogoyang.yaofan.meta.team.entity.MyTeam;
import com.gogoyang.yaofan.meta.team.entity.MyTeamView;
import com.gogoyang.yaofan.meta.team.entity.Team;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface TeamDao {
    void createTeam(Team team);

    Team getTeamByName(String name);

    void createMyTeam(MyTeam myTeam);

    ArrayList<MyTeamView> listTeam(Map qIn);
}
