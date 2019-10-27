package com.gogoyang.yaofan.meta.team.dao;

import com.gogoyang.yaofan.meta.team.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.jdbc.object.MappingSqlQueryWithParameters;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface TeamDao {
    void createTeam(Team team);

    Team getTeamByName(String name);

    void createMyTeam(MyTeam myTeam);

    ArrayList<MyTeamView> listTeam(Map qIn);

    /**
     * 根据团队名称关键字搜索团队
     *
     * @param qIn
     * @return
     */
    ArrayList<Team> searchTeam(Map qIn);

    TeamView getTeamByTeamId(String name);

    void createApplyTeam(ApplyTeam applyTeam);

    ArrayList<ApplyTeam> listApplyTeam(Map qIn);

    ApplyTeamView getApplyTeam(String teamId);

    /**
     * 统计团队或者某个用户未处理的申请数
     * @param qIn
     * @return
     */
    int totalApplyTeamUnProcess(Map qIn);
}
