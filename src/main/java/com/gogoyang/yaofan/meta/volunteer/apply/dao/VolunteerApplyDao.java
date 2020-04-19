package com.gogoyang.yaofan.meta.volunteer.apply.dao;

import com.gogoyang.yaofan.meta.volunteer.apply.entity.VolunteerApply;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VolunteerApplyDao {
    /**
     * 创建一个义工任务申请
     * @param volunteerApply
     */
    void createVolunteerApply(VolunteerApply volunteerApply);
}
