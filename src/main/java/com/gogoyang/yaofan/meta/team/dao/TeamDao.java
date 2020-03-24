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

    void createApplyTeam(ApplyTeam applyTeam);

    ArrayList<ApplyTeam> listApplyTeam(Map qIn);

    ApplyTeamView getApplyTeam(String applyId);

    /**
     * 统计团队或者某个用户未处理的申请数
     * @param qIn
     * @return
     */
    int totalApplyTeamUnProcess(Map qIn);

    void setApplyTeamReadTime(ApplyTeamView applyTeamView);

    void processApplyTeam(ApplyTeam applyTeam);

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
}
