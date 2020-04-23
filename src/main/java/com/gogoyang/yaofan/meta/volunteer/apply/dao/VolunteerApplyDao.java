package com.gogoyang.yaofan.meta.volunteer.apply.dao;

import com.gogoyang.yaofan.meta.volunteer.apply.entity.VolunteerApply;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface VolunteerApplyDao {
    /**
     * 创建一个义工任务申请
     * @param volunteerApply
     */
    void createVolunteerApply(VolunteerApply volunteerApply);

    /**
     * 查询义工任务申请列表
     * @param qIn
     * createUserId
     * applyUserId
     * volunteerTaskId
     * status
     * @return
     */
    ArrayList<VolunteerApply> listVolunteerApply(Map qIn);

    /**
     * 查询义工任务申请详情
     * @param volunteerApplyId
     * @return
     */
    VolunteerApply getVolunteerApply(String volunteerApplyId);

    /**
     * 处理义工任务申请
     * @param qIn
     * processResult
     * processTime
     * processRemark
     * processUserId
     * volunteerApplyId
     */
    void processVolunteerApply(Map qIn);

    /**
     * 查询所有已经通过审核的义工
     * @param qIn
     * createUserId
     * offset
     * size
     * @return
     */
    ArrayList<VolunteerApply> listMyVolunteerAgree(Map qIn);

    /**
     * 统计我创建的义工任务的未读义工申请
     * @param qIn
     * userId (必填，创建用户Id)
     * volunteerTaskId (选填，任务id，如果不填就是统计用户所有任务的未读)
     * @return
     */
    Integer totalMyVolunteerApplyUnread(Map qIn);

    /**
     * 统计我申请的义工任务已处理但未阅读的总数
     * @param userId
     * @return
     */
    Integer totalMyVolunteerApplyProcessUnread(String userId);

    /**
     * 设置义工任务申请的阅读时间
     * @param qIn
     * readTime （选填）
     * processReadTime （选填）
     * volunteerApplyId (必填）
     */
    void setReadTime(Map qIn);
}
