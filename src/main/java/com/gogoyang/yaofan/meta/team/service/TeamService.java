package com.gogoyang.yaofan.meta.team.service;

import com.gogoyang.yaofan.meta.team.dao.TeamApplyLogDao;
import com.gogoyang.yaofan.meta.team.dao.TeamDao;
import com.gogoyang.yaofan.meta.team.dao.TeamQuitDao;
import com.gogoyang.yaofan.meta.team.dao.TeamUserDao;
import com.gogoyang.yaofan.meta.team.entity.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class TeamService implements ITeamService {
    private final TeamDao teamDao;
    private final TeamApplyLogDao teamApplyLogDao;
    private final TeamUserDao teamUserDao;
    private final TeamQuitDao teamQuitDao;

    public TeamService(TeamDao teamDao,
                       TeamApplyLogDao teamApplyLogDao,
                       TeamUserDao teamUserDao,
                       TeamQuitDao teamQuitDao) {
        this.teamDao = teamDao;
        this.teamApplyLogDao = teamApplyLogDao;
        this.teamUserDao = teamUserDao;
        this.teamQuitDao = teamQuitDao;
    }

    /**
     * 创建一个团队
     *
     * @param team
     * @return
     * @throws Exception
     */
    @Override
    public Team createTeam(Team team) throws Exception {
        teamDao.createTeam(team);
        return team;
    }

    /**
     * 根据teamId读取团队详情
     *
     * @param qIn teamId
     *            teamName
     * @return
     * @throws Exception
     */
    @Override
    public Team getTeam(Map qIn) throws Exception {
        Team team = teamDao.getTeam(qIn);
        return team;
    }

    /**
     * 创建一个新的团队用户链接
     *
     * @param teamUser
     * @throws Exception
     */
    @Override
    public void createTeamUser(TeamUser teamUser) throws Exception {
        teamUserDao.createTeamUser(teamUser);
    }

    /**
     * @param qIn createUserId
     *            status
     *            teamId
     *            managerId
     *            teamName（模糊查询）
     *            offset
     *            size
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<Team> listTeam(Map qIn) throws Exception {
        ArrayList<Team> teams = teamDao.listTeam(qIn);
        return teams;
    }

    /**
     * 创建团队申请日志
     *
     * @param teamApplyLog
     * @throws Exception
     */
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
    public ArrayList<TeamApplyLog> listTeamApplyLog(Map qIn) throws Exception {
        ArrayList<TeamApplyLog> teamApplyLogs = teamApplyLogDao.listTeamApplyLog(qIn);
        return teamApplyLogs;
    }

    /**
     * 根据teamApplyLogId查询团队申请日志详情
     *
     * @param teamApplyLogId
     * @return
     * @throws Exception
     */
    @Override
    public TeamApplyLog getTeamApplyLog(String teamApplyLogId) throws Exception {
        TeamApplyLog teamApplyLog = teamApplyLogDao.getTeamApplyLog(teamApplyLogId);
        return teamApplyLog;
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
     * @param qIn readTime
     *            teamApplyLogId
     */
    @Override
    public void setTeamApplyLogReadTimeProcess(Map qIn) throws Exception {
        teamApplyLogDao.setTeamApplyLogReadTimeProcess(qIn);
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
    public Integer totalTeamApplyLogUnReadProcess(Map qIn) throws Exception {
        Integer total = teamApplyLogDao.totalTeamApplyLogUnReadProcess(qIn);
        return total;
    }

    /**
     * 查询一个团队用户信息
     *
     * @param qIn teamId
     *            managerId
     *            history
     *            userId
     *            status
     *            memberType
     *            offset
     *            size
     * @return
     */
    @Override
    public ArrayList<TeamUser> listTeamUser(Map qIn) throws Exception {
        ArrayList<TeamUser> teamUsers = teamUserDao.listTeamUser(qIn);
        return teamUsers;
    }

    /**
     * 创建退出团队日志
     *
     * @param teamQuitLog
     */
    @Override
    public void createTeamQuitLog(TeamQuitLog teamQuitLog) throws Exception {
        teamQuitDao.createTeamQuitLog(teamQuitLog);
    }

    /**
     * 查询退团日志列表
     *
     * @param qIn userId
     *            managerId
     *            offset
     *            size
     * @return
     */
    @Override
    public ArrayList<TeamQuitLog> listTeamQuitLog(Map qIn) throws Exception {
        ArrayList<TeamQuitLog> teamQuitLogs = teamQuitDao.listTeamQuitLog(qIn);
        return teamQuitLogs;
    }

    /**
     * 查询退团日志详情
     *
     * @param qIn teamQuitLogId
     * @return
     * @throws Exception
     */
    @Override
    public TeamQuitLog getTeamQuitLog(Map qIn) throws Exception {
        TeamQuitLog teamQuitLog = teamQuitDao.getTeamQuitLog(qIn);
        return teamQuitLog;
    }

    /**
     * 处理团队申请
     *
     * @param qIn status
     *            processTime
     *            processUserId
     */
    @Override
    public void processTeamQuitLog(Map qIn) {
        teamQuitDao.processTeamQuitLog(qIn);
    }

    /**
     * 统计我申请加入的团队申请总数
     *
     * @param qIn userId
     * @return
     */
    @Override
    public Integer totalTeamApplyLogMyApply(Map qIn) {
        Integer total = teamApplyLogDao.totalTeamApplyLogMyApply(qIn);
        return total;
    }

    /**
     * 统计申请加入我的团队的申请总数
     *
     * @param qIn teamList
     * @return
     */
    @Override
    public Integer totalTeamApplyLogMyTeam(Map qIn) {
        Integer total = teamApplyLogDao.totalTeamApplyLogMyTeam(qIn);
        return total;
    }

    /**
     * 把团队设置为失效状态
     * 假删除
     * @param qIn
     * status
     * teamId
     * lastUpdateTime
     * lastUpdateUserId
     * @throws Exception
     */
    @Override
    public void setTeamStatus(Map qIn) throws Exception {
        teamDao.setTeamStatus(qIn);
    }

    /**
     *
     * @param qIn
     * status
     * ids
     * memberType
     */
    @Override
    public void updateTeamUser(Map qIn) {
        teamUserDao.updateTeamUser(qIn);
    }
}
