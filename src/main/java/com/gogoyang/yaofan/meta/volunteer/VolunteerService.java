package com.gogoyang.yaofan.meta.volunteer;

import com.gogoyang.yaofan.meta.volunteer.apply.dao.VolunteerApplyDao;
import com.gogoyang.yaofan.meta.volunteer.apply.entity.VolunteerApply;
import com.gogoyang.yaofan.meta.volunteer.comment.dao.VolunteerCommentDao;
import com.gogoyang.yaofan.meta.volunteer.comment.entity.VolunteerComment;
import com.gogoyang.yaofan.meta.volunteer.task.dao.VolunteerTaskDao;
import com.gogoyang.yaofan.meta.volunteer.task.entity.VolunteerTask;
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
    public void createVolunteerTask(VolunteerTask volunteerTask) throws Exception {
        volunteerTaskDao.createVolunteerTask(volunteerTask);
    }

    /**
     * 创建一个义工任务申请
     *
     * @param volunteerApply
     */
    @Override
    public void createVolunteerApply(VolunteerApply volunteerApply) throws Exception {
        volunteerApplyDao.createVolunteerApply(volunteerApply);
    }

    /**
     * 创建一个义工任务评价
     *
     * @param volunteerComment
     */
    @Override
    public void createVolunteerComment(VolunteerComment volunteerComment) throws Exception {
        volunteerCommentDao.createVolunteerComment(volunteerComment);
    }

    /**
     * 读取义工任务列表
     *包括我创建的，和我承接的义工任务
     * @param qIn createUserId
     *            applyUserId
     *            status
     *            offset
     *            size
     * @return
     */
    @Override
    public ArrayList<VolunteerTask> listVolunteerTask(Map qIn) throws Exception {
        ArrayList<VolunteerTask> volunteerTasks = volunteerTaskDao.listVolunteerTask(qIn);
        return volunteerTasks;
    }

    /**
     * 根据volunteerTaskId查询义工任务详情
     *
     * @param volunteerTaskId
     * @return
     */
    @Override
    public VolunteerTask getVolunteerTaskDetail(String volunteerTaskId) throws Exception {
        VolunteerTask volunteerTask = volunteerTaskDao.getVolunteerTaskDetail(volunteerTaskId);
        return volunteerTask;
    }

    /**
     * 根据volunteerTaskId查询义工任务，不包括详情
     *
     * @param volunteerTaskId
     * @return
     */
    @Override
    public VolunteerTask getVolunteerTaskTiny(String volunteerTaskId) throws Exception {
        VolunteerTask volunteerTask = volunteerTaskDao.getVolunteerTaskTiny(volunteerTaskId);
        return volunteerTask;
    }

    /**
     * 查询义工任务申请列表
     *
     * @param qIn
     * createUserId
     * applyUserId
     * volunteerTaskId
     * status
     * @return
     */
    @Override
    public ArrayList<VolunteerApply> listVolunteerApply(Map qIn) throws Exception {
        ArrayList<VolunteerApply> volunteerApplies = volunteerApplyDao.listVolunteerApply(qIn);
        return volunteerApplies;
    }

    /**
     * 查询义工任务申请详情
     *
     * @param volunteerApplyId
     * @return
     */
    @Override
    public VolunteerApply getVolunteerApply(String volunteerApplyId) throws Exception {
        VolunteerApply volunteerApply = volunteerApplyDao.getVolunteerApply(volunteerApplyId);
        return volunteerApply;
    }

    /**
     * 处理义工任务申请
     * @param qIn
     * processResult
     * processTime
     * processRemark
     * processUserId
     * @throws Exception
     */
    @Override
    public void processVolunteerApply(Map qIn) throws Exception {
        volunteerApplyDao.processVolunteerApply(qIn);
    }

    /**
     * 查询所有已经通过审核的义工
     * @param qIn
     * createUserId
     * offset
     * size
     * @return
     */
    @Override
    public ArrayList<VolunteerApply> listMyVolunteerAgree(Map qIn) {
        ArrayList<VolunteerApply> volunteerApplies=volunteerApplyDao.listMyVolunteerAgree(qIn);
        return volunteerApplies;
    }

    /**
     * 统计我的所有义工任务数量
     * @param qIn createUserId
     *            applyUserId
     * @return
     */
    @Override
    public Integer totalMyVolunteerTasks(Map qIn) {
        Integer total=volunteerTaskDao.totalMyVolunteerTasks(qIn);
        return total;
    }

    /**
     *
     * @param qIn
     * userId
     * status
     * offset
     * size
     * @return
     */
    @Override
    public ArrayList<VolunteerTask> listMyVolunteerTask(Map qIn) {
        ArrayList<VolunteerTask> volunteerTasks=volunteerTaskDao.listMyVolunteerTask(qIn);
        return volunteerTasks;
    }

    /**
     * 设置义工任务状态
     * @param qIn
     * status
     */
    @Override
    public void setVolunteerTaskStatus(Map qIn) {
        volunteerTaskDao.setVolunteerTaskStatus(qIn);
    }

    /**
     * 统计我创建的义工任务的未读义工申请
     * @param qIn
     * userId (必填，创建用户Id)
     * volunteerTaskId (选填，任务id，如果不填就是统计用户所有任务的未读)
     * @return
     */
    @Override
    public Integer totalMyVolunteerApplyUnread(Map qIn) throws Exception{
        Integer total=volunteerApplyDao.totalMyVolunteerApplyUnread(qIn);
        return total;
    }

    /**
     * 统计我申请的义工任务已处理但未阅读的总数
     * @param userId
     * @return
     */
    @Override
    public Integer totalMyVolunteerApplyProcessUnread(String userId) {
        Integer total=volunteerApplyDao.totalMyVolunteerApplyProcessUnread(userId);
        return total;
    }

    /**
     * 设置义工任务申请的阅读时间
     * @param qIn
     * readTime （选填）
     * processReadTime （选填）
     */
    @Override
    public void setReadTime(Map qIn) {
        volunteerApplyDao.setReadTime(qIn);
    }
}
