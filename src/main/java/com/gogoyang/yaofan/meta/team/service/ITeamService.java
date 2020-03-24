package com.gogoyang.yaofan.meta.team.service;

import com.gogoyang.yaofan.meta.team.entity.*;

import java.util.ArrayList;
import java.util.Map;

public interface ITeamService {
    Team createTeam(Team team) throws Exception;

    Team getTeamByName(String name) throws Exception;

    MyTeam createMyTeam(MyTeam myTeam) throws Exception;

    /**
     * 查询团队列表
     * @param qIn
     * userId
     * status
     * teamId
     * @return
     * @throws Exception
     */
    ArrayList<MyTeamView> listTeam(Map qIn) throws Exception;

    /**
     * 根据团队名称关键字搜索团队
     * @param qIn
     * @return
     * @throws Exception
     */
    ArrayList<Team> searchTeam(Map qIn) throws Exception;

    TeamView getTeamByTeamId(String teamId) throws Exception;

    void createApplyTeam(ApplyTeam applyTeam) throws Exception;

    ArrayList<ApplyTeam> listApplyTeam(Map qIn) throws Exception;

    ApplyTeamView getApplyTeam(String applyId) throws Exception;

    /**
     * 统计团队或者某个用户未处理的申请数
     * @param qIn
     * @return
     * @throws Exception
     */
    int totalApplyTeamUnProcess(Map qIn) throws Exception;

    void setApplyTeamReadTime(ApplyTeamView applyTeamView) throws Exception;

    void processApplyTeam(ApplyTeam applyTeam) throws Exception;

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
