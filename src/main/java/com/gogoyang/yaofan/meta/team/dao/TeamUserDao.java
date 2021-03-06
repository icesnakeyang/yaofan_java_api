package com.gogoyang.yaofan.meta.team.dao;

import com.gogoyang.yaofan.meta.team.entity.TeamUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface TeamUserDao {
    /**
     * 创建一个新的团队用户链接
     * @param teamUser
     */
    void createTeamUser(TeamUser teamUser);

    /**
     * 查询团队成员列表
     * @param qIn
     * managerId
     * history
     * teamId
     * userId
     * status
     * memberType
     * offset
     * size
     * @return
     */
    ArrayList<TeamUser> listTeamUser(Map qIn);

    /**
     * 修改用户在团队的信息
     * @param qIn
     * status
     * ids
     * memberType
     */
    void updateTeamUser(Map qIn);
}
