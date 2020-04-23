package com.gogoyang.yaofan.meta.volunteer.task.dao;

import com.gogoyang.yaofan.meta.volunteer.task.entity.VolunteerTask;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface VolunteerTaskDao {
    /**
     * 创建一个义工任务
     *
     * @param volunteerTask
     */
    void createVolunteerTask(VolunteerTask volunteerTask);

    /**
     * 读取义工任务列表
     * 包括我创建的，和我承接的义工任务
     * @param qIn createUserId
     *            applyUserId
     *            status
     *            offset
     *            size
     * @return
     */
    ArrayList<VolunteerTask> listVolunteerTask(Map qIn);

    /**
     * 根据volunteerTaskId查询义工任务详情
     *
     * @param volunteerTaskId
     * @return
     */
    VolunteerTask getVolunteerTaskDetail(String volunteerTaskId);

    /**
     * 根据volunteerTaskId查询义工任务，不包括详情
     *
     * @param volunteerTaskId
     * @return
     */
    VolunteerTask getVolunteerTaskTiny(String volunteerTaskId);

    /**
     * 统计我的所有义工任务数量
     *
     * @param qIn createUserId
     *            applyUserId
     * @return
     */
    Integer totalMyVolunteerTasks(Map qIn);

    /**
     * 读取我的义工任务列表
     * @param qIn
     * userId
     * status
     * offset
     * size
     * @return
     */
    ArrayList<VolunteerTask> listMyVolunteerTask(Map qIn);

    /**
     * 设置义工任务状态
     * @param qIn
     * status
     * volunteerTaskId
     */
    void setVolunteerTaskStatus(Map qIn);
}
