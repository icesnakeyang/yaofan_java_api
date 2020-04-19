package com.gogoyang.yaofan.meta.volunteer.task.dao;

import com.gogoyang.yaofan.meta.volunteer.task.entity.VolunteerTask;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VolunteerTaskDao {
    /**
     * 创建一个义工任务
     * @param volunteerTask
     */
    void createVolunteerTask(VolunteerTask volunteerTask);
}
