package com.gogoyang.yaofan.meta.volunteer;

import com.gogoyang.yaofan.meta.volunteer.apply.entity.VolunteerApply;
import com.gogoyang.yaofan.meta.volunteer.comment.entity.VolunteerComment;
import com.gogoyang.yaofan.meta.volunteer.task.entity.VolunteerTask;

public interface IVolunteerService {
    /**
     * 创建一个义工任务
     * @param volunteerTask
     */
    void createVolunteerTask(VolunteerTask volunteerTask);

    /**
     * 创建一个义工任务申请
     * @param volunteerApply
     */
    void createVolunteerApply(VolunteerApply volunteerApply);

    /**
     * 创建一个义工任务评价
     * @param volunteerComment
     */
    void createVolunteerComment(VolunteerComment volunteerComment);
}
