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

    TeamApplyView getTeamApplyLog(String applyId) throws Exception;

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
     * @throws Exception
     */
    void setTeamApplyLogReadTime(Map qIn) throws Exception;

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
     * 查询所有我创建的团队
     * @param qIn
     * userId:创建人Id
     * status：状态(可选)
     * @return
     */
    ArrayList<TeamView> listMyCreateTeam(Map qIn) throws Exception;

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
}
