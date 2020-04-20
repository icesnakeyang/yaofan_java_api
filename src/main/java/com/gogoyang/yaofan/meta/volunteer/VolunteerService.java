package com.gogoyang.yaofan.meta.volunteer;

import com.gogoyang.yaofan.meta.volunteer.apply.dao.VolunteerApplyDao;
import com.gogoyang.yaofan.meta.volunteer.apply.entity.VolunteerApply;
import com.gogoyang.yaofan.meta.volunteer.comment.dao.VolunteerCommentDao;
import com.gogoyang.yaofan.meta.volunteer.comment.entity.VolunteerComment;
import com.gogoyang.yaofan.meta.volunteer.task.dao.VolunteerTaskDao;
import com.gogoyang.yaofan.meta.volunteer.task.entity.VolunteerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class VolunteerService implements IVolunteerService {
    private final VolunteerTaskDao volunteerTaskDao;
    private final VolunteerApplyDao volunteerApplyDao;
    private final VolunteerCommentDao volunteerCommentDao;

    public VolunteerService(VolunteerTaskDao volunteerTaskDao,
                            VolunteerApplyDao volunteerApplyDao,
                            VolunteerCommentDao volunteerCommentDao) {
        this.volunteerTaskDao = volunteerTaskDao;
        this.volunteerApplyDao = volunteerApplyDao;
        this.volunteerCommentDao = volunteerCommentDao;
    }

    /**
     * 创建一个义工任务
     *
     * @param volunteerTask
     */
    @Override
    public void createVolunteerTask(VolunteerTask volunteerTask) {
        volunteerTaskDao.createVolunteerTask(volunteerTask);
    }

    /**
     * 创建一个义工任务申请
     *
     * @param volunteerApply
     */
    @Override
    public void createVolunteerApply(VolunteerApply volunteerApply) {
        volunteerApplyDao.createVolunteerApply(volunteerApply);
    }

    /**
     * 创建一个义工任务评价
     *
     * @param volunteerComment
     */
    @Override
    public void createVolunteerComment(VolunteerComment volunteerComment) {
        volunteerCommentDao.createVolunteerComment(volunteerComment);
    }

    /**
     * 读取义工任务列表
     * @param qIn
     * createUserId
     * status
     * offset
     * size
     * @return
     */
    @Override
    public ArrayList<VolunteerTask> listVolunteerTask(Map qIn) {
        ArrayList<VolunteerTask> volunteerTasks = volunteerTaskDao.listVolunteerTask(qIn);
        return volunteerTasks;
    }

    /**
     * 根据volunteerTaskId查询义工任务详情
     * @param volunteerTaskId
     * @return
     */
    @Override
    public VolunteerTask getVolunteerTaskDetail(String volunteerTaskId) {
        VolunteerTask volunteerTask=volunteerTaskDao.getVolunteerTaskDetail(volunteerTaskId);
        return volunteerTask;
    }
}
