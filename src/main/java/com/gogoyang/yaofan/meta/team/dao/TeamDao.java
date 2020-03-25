package com.gogoyang.yaofan.meta.team.dao;

import com.gogoyang.yaofan.meta.team.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface TeamDao {
    void createTeam(Team team);

    Team getTeamByName(String name);

    void createMyTeam(MyTeam myTeam);

    /**
     * 查询团队列表
     * @param qIn
     * userId
     * status
     * teamId
     * @return
     */
    ArrayList<MyTeamView> listTeam(Map qIn);

    /**
     * 根据团队名称关键字搜索团队
     *
     * @param qIn
     * @return
     */
    ArrayList<Team> searchTeam(Map qIn);

    TeamView getTeamByTeamId(String name);

    /**
     * 修改团队信息
     * @param qIn
     * name
     * description
     * where teamId
     */
    void updateTeam(Map qIn);

    /**
     * 删除一个团队
     * @param teamId
     */
    void deleteTeam(String teamId);

    /**
     * 查询所有我创建的团队
     * @param qIn
     * userId:创建人Id
     * status：状态(可选)
     * @return
     */
    ArrayList<TeamView> listMyCreateTeam(Map qIn);
}
