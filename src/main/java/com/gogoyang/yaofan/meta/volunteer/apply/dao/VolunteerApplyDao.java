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
}
