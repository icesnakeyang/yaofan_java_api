package com.gogoyang.yaofan.meta.team.dao;

import com.gogoyang.yaofan.meta.team.entity.ApplyTeamLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface ApplyTeamLogDao {
    /**
     * 创建团队申请日志
     * @param applyTeam
     */
    void createApplyTeamLog(ApplyTeamLog applyTeam);

    /**
     * 读取团队申请日志列表
     * @param qIn
     * @return
     */
    ArrayList<ApplyTeamLog> listApplyTeam(Map qIn);

    /**
     * 处理团队申请日志
     * @param applyTeam
     */
    void processApplyTeam(ApplyTeamLog applyTeam);
}
