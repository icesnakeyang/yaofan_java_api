package com.gogoyang.yaofan.meta.volunteer.task.dao;

import com.gogoyang.yaofan.meta.volunteer.task.entity.VolunteerTask;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface VolunteerTaskDao {
    /**
     * 创建一个义工任务
     * @param volunteerTask
     */
    void createVolunteerTask(VolunteerTask volunteerTask);

    /**
     * 读取义工任务列表
     * @param qIn
     * createUserId
     * status
     * offset
     * size
     * @return
     */
    ArrayList<VolunteerTask> listVolunteerTask(Map qIn);

    /**
     * 根据volunteerTaskId查询义工任务详情
     * @param volunteerTaskId
     * @return
     */
    VolunteerTask getVolunteerTaskDetail(String volunteerTaskId);

}
