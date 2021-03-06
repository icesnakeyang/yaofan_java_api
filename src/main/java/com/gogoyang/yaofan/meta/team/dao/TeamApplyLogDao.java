package com.gogoyang.yaofan.meta.team.dao;

import com.gogoyang.yaofan.meta.team.entity.TeamApplyLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface TeamApplyLogDao {
    /**
     * 创建团队申请日志
     * @param teamApplyLog
     */
    void createTeamApplyLog(TeamApplyLog teamApplyLog);

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
    ArrayList<TeamApplyLog> listTeamApplyLog(Map qIn);

    /**
     * 根据teamApplyLogId查询团队申请日志详情
     * @param teamApplyLogId
     * @return
     */
    TeamApplyLog getTeamApplyLog(String teamApplyLogId);

    /**
     * 处理团队申请日志
     * @param qIn
     * teamApplyLogId(必填)
     * status
     * processRemark
     * processTime
     * processUserId
     */
    void processTeamApplyLog(Map qIn);

    /**
     * 设置团队申请日志的阅读时间
     * @param qIn
     * readTime
     * teamApplyLogId
     */
    void setTeamApplyLogReadTime(Map qIn);

    /**
     * 设置团队申请日志的处理结果阅读时间
     * @param qIn
     * readTime
     * teamApplyLogId
     */
    void setTeamApplyLogReadTimeProcess(Map qIn);

    /**
     * 统计用户未读的团队申请日志
     * @param qIn
     * userId:当前用户id
     * teamList：当前用户创建的团队结合
     * @return
     */
    Integer totalTeamApplyLogUnRead(Map qIn);

    /**
     * 统计团队或者某个用户未处理的申请数
     * @param qIn
     * @return
     */
    Integer totalApplyTeamUnProcess(Map qIn);

    /**
     * 取消加入团队申请
     * @param qIn
     * status
     * teamApplyLogId
     */
    void cancelTeamApplyLog(Map qIn);

    /**
     * 统计用户未阅读的团队申请日志处理结果的数量
     * @param qIn
     * userId
     * @return
     */
    Integer totalTeamApplyLogUnReadProcess(Map qIn);

    /**
     * 统计我申请加入的团队申请总数
     * @param qIn
     * userId
     * @return
     */
    Integer totalTeamApplyLogMyApply(Map qIn);

    /**
     * 统计申请加入我的团队的申请总数
     * @param qIn
     * teamList
     * @return
     */
    Integer totalTeamApplyLogMyTeam(Map qIn);
}
