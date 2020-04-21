package com.gogoyang.yaofan.meta.volunteer;

import com.gogoyang.yaofan.meta.volunteer.apply.entity.VolunteerApply;
import com.gogoyang.yaofan.meta.volunteer.comment.entity.VolunteerComment;
import com.gogoyang.yaofan.meta.volunteer.task.entity.VolunteerTask;

import java.util.ArrayList;
import java.util.Map;

public interface IVolunteerService {
    /**
     * 创建一个义工任务
     * @param volunteerTask
     */
    void createVolunteerTask(VolunteerTask volunteerTask) throws Exception;

    /**
     * 创建一个义工任务申请
     * @param volunteerApply
     */
    void createVolunteerApply(VolunteerApply volunteerApply) throws Exception;

    /**
     * 创建一个义工任务评价
     * @param volunteerComment
     */
    void createVolunteerComment(VolunteerComment volunteerComment) throws Exception;

    /**
     * 读取义工任务列表
     * @param qIn
     * createUserId
     * status
     * offset
     * size
     * @return
     */
    ArrayList<VolunteerTask> listVolunteerTask(Map qIn) throws Exception;

    /**
     * 根据volunteerTaskId查询义工任务详情
     * @param volunteerTaskId
     * @return
     */
    VolunteerTask getVolunteerTaskDetail(String volunteerTaskId) throws Exception;

    /**
     * 根据volunteerTaskId查询义工任务，不包括详情
     * @param volunteerTaskId
     * @return
     */
    VolunteerTask getVolunteerTaskTiny(String volunteerTaskId) throws Exception;

    /**
     * 查询义工任务申请列表
     * @param qIn
     * createUserId
     * applyUserId
     * volunteerTaskId
     * status
     * @return
     */
    ArrayList<VolunteerApply> listVolunteerApply(Map qIn) throws Exception;

    /**
     * 查询义工任务申请详情
     * @param volunteerApplyId
     * @return
     */
    VolunteerApply getVolunteerApply(String volunteerApplyId) throws Exception;

    /**
     * 处理义工任务申请
     * @param qIn
     * processResult
     * processTime
     * processRemark
     * processUserId
     * volunteerApplyId
     */
    void processVolunteerApply(Map qIn) throws Exception;

    /**
     * 查询所有已经通过审核的义工
     * @param qIn
     * createUserId
     * offset
     * size
     * @return
     */
    ArrayList<VolunteerApply> listMyVolunteerAgree(Map qIn);
}
