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

    /**
     * 读取团队申请日志列表
     *
     * @param qIn applyUserId
     *            processUserId
     *            status
     *            teamList
     *            offset
     *            size
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<TeamApplyView> listTeamApplyLog(Map qIn) throws Exception {
        ArrayList<TeamApplyView> teamApplyViews = teamApplyLogDao.listTeamApplyLog(qIn);
        return teamApplyViews;
    }

    @Override
    public TeamApplyView getTeamApplyLog(String teamApplyLogId) throws Exception {
        TeamApplyView teamApplyView = teamApplyLogDao.getTeamApplyLog(teamApplyLogId);
        return teamApplyView;
    }

    /**
     * @param qIn userId:当前用户id
     *            teamList：当前用户创建的团队结合
     * @return
     */
    @Override
    public Integer totalTeamApplyLogUnRead(Map qIn) throws Exception {
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
     * @param qIn readTime
     *            teamApplyLogId
     * @throws Exception
     */
    @Override
    public void setTeamApplyLogReadTime(Map qIn) throws Exception {
        teamApplyLogDao.setTeamApplyLogReadTime(qIn);
    }

    /**
     * @param qIn teamApplyLogId(必填)
     *            status
     *            processRemark
     *            processTime
     * @throws Exception
     */
    @Override
    public void processTeamApplyLog(Map qIn) throws Exception {
        teamApplyLogDao.processTeamApplyLog(qIn);
    }

    @Override
    public void updateTeam(Map qIn) throws Exception {
        teamDao.updateTeam(qIn);
    }

    /**
     * @param teamId
     */
    @Override
    public void deleteTeam(String teamId) throws Exception {
        teamDao.deleteTeam(teamId);
    }


    /**
     * @param qIn userId:创建人Id
     *            status：状态(可选)
     * @return
     */
    @Override
    public ArrayList<TeamView> listMyCreateTeam(Map qIn) throws Exception {
        ArrayList<TeamView> teamViews = teamDao.listMyCreateTeam(qIn);
        return teamViews;
    }

    /**
     * @param qIn status
     *            teamApplyLogId
     */
    @Override
    public void cancelTeamApplyLog(Map qIn) throws Exception {
        teamApplyLogDao.cancelTeamApplyLog(qIn);
    }

    /**
     * @param qIn userId
     * @return
     */
    @Override
    public Integer totalTeamApplyLogUnReadProcess(Map qIn) {
        Integer total = teamApplyLogDao.totalTeamApplyLogUnReadProcess(qIn);
        return total;
    }

    /**
     * 设置我未阅读的处理结果的阅读时间
     * @param qIn
     * processReadTime
     * teamApplyLogId
     */
    @Override
    public void setTeamApplyLogReadTimeUnProcess(Map qIn) {
        teamApplyLogDao.setTeamApplyLogReadTimeUnProcess(qIn);
    }
}
