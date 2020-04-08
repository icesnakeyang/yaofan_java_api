package com.gogoyang.yaofan.meta.team.dao;

import com.gogoyang.yaofan.meta.team.entity.TeamQuitLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface TeamQuitDao {
    /**
     * 创建退出团队日志
     * @param teamQuitLog
     */
    void createTeamQuitLog(TeamQuitLog teamQuitLog);

    /**
     * 查询退团日志列表
     * @param qIn
     * userId
     * managerId
     * offset
     * size
     * @return
     */
    ArrayList<TeamQuitLog> listTeamQuitLog(Map qIn);

    /**
     * 查询退团日志详情
     * @param qIn
     * teamQuitLogId
     * @return
     */
    TeamQuitLog getTeamQuitLog(Map qIn);

    /**
     * 处理团队申请
     * @param qIn
     * status
     * processTime
     * processUserId
     * processRemark
     */
    void processTeamQuitLog(Map qIn);
}
