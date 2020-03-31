package com.gogoyang.yaofan.meta.team.service;

import com.gogoyang.yaofan.meta.team.entity.*;

import java.util.ArrayList;
import java.util.Map;

public interface ITeamService {
    /**
     * 创建一个团队
     * @param team
     * @return
     * @throws Exception
     */
    Team createTeam(Team team) throws Exception;

    /**
     * 根据teamId读取团队详情
     * @param qIn
     * teamId
     * teamName
     * @return
     * @throws Exception
     */
    Team getTeam(Map qIn) throws Exception;

    /**
     * 创建一个新的团队用户链接
     * @param teamUser
     * @return
     * @throws Exception
     */
    void createTeamUser(TeamUser teamUser) throws Exception;

    /**
     * 查询团队列表
     * @param qIn
     * createUserId
     * status
     * teamId
     * managerId
     * teamName（模糊查询）
     * offset
     * size
     * @return
     */
    ArrayList<Team> listTeam(Map qIn) throws Exception;

    /**
     * 创建团队申请日志
     * @param teamApplyLog
     * @throws Exception
     */
    void createTeamApplyLog(TeamApplyLog teamApplyLog) throws Exception;

    /**
     * 读取团队申请日志列表
     * @param qIn
     * applyUserId
     * processUserId
     * status
     * teamList
     * offset
     * size
     * @return
     */
    ArrayList<TeamApplyLog> listTeamApplyLog(Map qIn) throws Exception;

    /**
     * 根据teamApplyLogId查询团队申请日志详情
     * @param teamApplyLogId
     * @return
     * @throws Exception
     */
    TeamApplyLog getTeamApplyLog(String teamApplyLogId) throws Exception;

    /**
     * 统计用户未读的团队申请日志
     * @param qIn
     * userId:当前用户id
     * teamList：当前用户创建的团队结合
     * @return
     */
    Integer totalTeamApplyLogUnRead(Map qIn) throws Exception;

    /**
     * 统计团队或者某个用户未处理的申请数
     * @param qIn
     * @return
     * @throws Exception
     */
    Integer totalApplyTeamUnProcess(Map qIn) throws Exception;

    /**
     * 设置团队申请日志的阅读时间
     * @param qIn
     * readTime
     * teamApplyLogId
     * @throws Exception
     */
    void setTeamApplyLogReadTime(Map qIn) throws Exception;

    /**
     * 设置团队申请日志的处理结果阅读时间
     * @param qIn
     * readTime
     * teamApplyLogId
     */
    void setTeamApplyLogReadTimeProcess(Map qIn);

    /**
     * 处理团队申请日志
     * @param qIn
     * teamApplyLogId(必填)
     * status
     * processRemark
     * processTime
     * processUserId
     */
    void processTeamApplyLog(Map qIn) throws Exception;

    /**
     * 修改团队信息
     * @param qIn
     * name
     * description
     * where teamId
     */
    void updateTeam(Map qIn) throws Exception;

    /**
     * 删除一个团队
     * @param teamId
     */
    void deleteTeam(String teamId) throws Exception;

    /**
     * 取消加入团队申请
     * @param qIn
     * status
     * teamApplyLogId
     */
    void cancelTeamApplyLog(Map qIn) throws Exception;

    /**
     * 统计用户未阅读的团队申请日志处理结果的数量
     * @param qIn
     * userId
     * @return
     */
    Integer totalTeamApplyLogUnReadProcess(Map qIn);

    /**
     * 查询一个团队用户信息
     * @param qIn
     * teamId
     * userId
     * status
     * @return
     */
    ArrayList<TeamUser> listTeamUser(Map qIn);

    /**
     * 创建退出团队日志
     * @param teamQuitLog
     */
    void createTeamQuitLog(TeamQuitLog teamQuitLog);
}
