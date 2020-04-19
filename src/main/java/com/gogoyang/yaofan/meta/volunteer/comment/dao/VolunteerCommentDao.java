package com.gogoyang.yaofan.meta.volunteer.comment.dao;

import com.gogoyang.yaofan.meta.volunteer.comment.entity.VolunteerComment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VolunteerCommentDao {
    /**
     * 创建一个义工任务评价
     * @param volunteerComment
     */
    void createVolunteerComment(VolunteerComment volunteerComment);
}
