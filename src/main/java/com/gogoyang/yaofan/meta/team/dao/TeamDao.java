package com.gogoyang.yaofan.meta.team.dao;

import com.gogoyang.yaofan.meta.team.entity.Team;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeamDao {
    void createTeam(Team team);

    Team getTeamByName(String name);
}
