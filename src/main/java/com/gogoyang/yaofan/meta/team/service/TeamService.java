package com.gogoyang.yaofan.meta.team.service;

import com.gogoyang.yaofan.meta.team.dao.TeamApplyLogDao;
import com.gogoyang.yaofan.meta.team.dao.TeamDao;
import com.gogoyang.yaofan.meta.team.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class TeamService implements ITeamService {
    private final TeamDao teamDao;
    private final TeamApplyLogDao teamApplyLogDao;

    public TeamService(TeamDao teamDao,
                       TeamApplyLogDao teamApplyLogDao) {
        this.teamDao = teamDao;
        this.teamApplyLogDao = teamApplyLogDao;
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

    /**
     * @param qIn userId
     *            status
     *            teamId
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<MyTeamView> listTeam(Map qIn) throws Exception {
        ArrayList<MyTeamView> myTeamViewArrayList = teamDao.listTeam(qIn);
        return myTeamViewArrayList;
    }

    /**
     * 根据团队名称关键字搜索团队
     *
     * @param qIn
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<Team> searchTeam(Map qIn) throws Exception {
        ArrayList<Team> teams = teamDao.searchTeam(qIn);
        return teams;
    }

    @Override
    public TeamView getTeamByTeamId(String teamId) throws Exception {
        TeamView team = teamDao.getTeamByTeamId(teamId);
        return team;
    }

    @Override
    public void createTeamApplyLog(TeamApplyLog teamApplyLog) throws Exception {
        teamApplyLogDao.createTeamApplyLog(teamApplyLog);
    }

    @Override
    public ArrayList<TeamApplyLog> listTeamApplyLog(Map qIn) throws Exception {
        ArrayList<TeamApplyLog> teamApplyLogs = teamApplyLogDao.listTeamApplyLog(qIn);
        return teamApplyLogs;
    }

    @Override
    public TeamApplyView getApplyTeam(String teamApplyLogId) throws Exception {
        TeamApplyView teamApplyView = teamApplyLogDao.getTeamApply(teamApplyLogId);
        return teamApplyView;
    }

    /**
     * @param qIn userId:当前用户id
     *            teamList：当前用户创建的团队结合
     * @return
     */
    @Override
    public Integer totalTeamApplyLogUnRead(Map qIn) {
        Integer total = teamApplyLogDao.totalTeamApplyLogUnRead(qIn);
        return total;
    }

    /**
     * 统计团队或者某个用户未处理的申请数
     *
     * @param qIn
     * @return
     * @throws Exception
     */
    @Override
    public Integer totalApplyTeamUnProcess(Map qIn) throws Exception {
        int total = teamApplyLogDao.totalApplyTeamUnProcess(qIn);
        return total;
    }

    /**
     * 设置团队申请日志的阅读时间
     *
     * @param qIn
     * @throws Exception
     */
    @Override
    public void setTeamApplyLogReadTime(Map qIn) throws Exception {
        teamApplyLogDao.setTeamApplyLogReadTime(qIn);
    }

    @Override
    public void processTeamApplyLog(Map qIn) throws Exception {
        teamApplyLogDao.processTeamApplyLog(qIn);
    }

    @Override
    public void updateTeam(Map qIn) {
        teamDao.updateTeam(qIn);
    }

    /**
     * @param teamId
     */
    @Override
    public void deleteTeam(String teamId) {
        teamDao.deleteTeam(teamId);
    }


    /**
     *
     * @param qIn
     * userId:创建人Id
     * status：状态(可选)
     * @return
     */
    @Override
    public ArrayList<TeamView> listMyCreateTeam(Map qIn) {
        ArrayList<TeamView> teamViews = teamDao.listMyCreateTeam(qIn);
        return teamViews;
    }
}
