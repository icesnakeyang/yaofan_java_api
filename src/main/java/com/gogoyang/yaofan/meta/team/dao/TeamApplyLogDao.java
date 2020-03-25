package com.gogoyang.yaofan.meta.team.dao;

import com.gogoyang.yaofan.meta.team.entity.TeamApplyLog;
import com.gogoyang.yaofan.meta.team.entity.TeamApplyView;
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
     * @return
     */
    ArrayList<TeamApplyLog> listTeamApplyLog(Map qIn);

    TeamApplyView getTeamApply(String teamApplyLogId);

    /**
     * 处理团队申请日志
     * @param qIn
     */
    void processTeamApplyLog(Map qIn);

    /**
     * 设置团队申请日志的阅读时间
     * @param qIn
     */
    void setTeamApplyLogReadTime(Map qIn);

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
}