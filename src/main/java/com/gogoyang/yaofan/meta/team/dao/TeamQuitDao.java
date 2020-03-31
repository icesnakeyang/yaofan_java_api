package com.gogoyang.yaofan.meta.team.dao;

import com.gogoyang.yaofan.meta.team.entity.TeamQuitLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeamQuitDao {
    /**
     * 创建退出团队日志
     * @param teamQuitLog
     */
    void createTeamQuitLog(TeamQuitLog teamQuitLog);
}
