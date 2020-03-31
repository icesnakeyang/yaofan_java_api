package com.gogoyang.yaofan.meta.team.dao;

import com.gogoyang.yaofan.meta.team.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface TeamDao {
    /**
     * 创建一个团队
     * @param team
     */
    void createTeam(Team team);

    /**
     * 根据teamId读取团队详情
     * @param qIn
     * teamId
     * teamName
     * @return
     */
    Team getTeam(Map qIn);

    /**
     * 查询团队列表
     * @param qIn
     * createUserId
     * status
     * teamId
     * managerId
     * teamName（模糊查询）
     * @return
     */
    ArrayList<Team> listTeam(Map qIn);

    /**
     * 修改团队信息
     * @param qIn
     * name
     * description
     * where teamId
     */
    void updateTeam(Map qIn);

    /**
     * 删除一个团队
     * @param teamId
     */
    void deleteTeam(String teamId);
}
