package com.gogoyang.yaofan.meta.stop.service;

import com.gogoyang.yaofan.meta.stop.dao.TaskStopDao;
import com.gogoyang.yaofan.meta.stop.entity.TaskStop;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TaskStopService implements ITaskStopService {
    private final TaskStopDao taskStopDao;

    public TaskStopService(TaskStopDao taskStopDao) {
        this.taskStopDao = taskStopDao;
    }

    /**
     * 创建任务停止日志
     * @param taskStop
     */
    @Override
    public void createTaskStop(TaskStop taskStop) {
        taskStopDao.createTaskStop(taskStop);
    }

    /**
     * 读取终止任务记录
     * @param qIn
     * taskId
     * createUserId
     * taskStopId
     * @return
     */
    @Override
    public TaskStop getTaskStop(Map qIn) {
        TaskStop taskStop=taskStopDao.getTaskStop(qIn);
        return taskStop;
    }

    /**
     * 统计未阅读的终止日志
     * @param qIn
     * taskId
     * partyAId
     * partyBId
     * @return
     */
    @Override
    public Integer totalTaskStopUnread(Map qIn) {
        Integer total=taskStopDao.totalTaskStopUnread(qIn);
        return total;
    }

    /**
     * 统计终止任务日志数
     * @param taskId
     * @return
     */
    @Override
    public Integer totalTaskStop(String taskId) {
        Integer total=taskStopDao.totalTaskStop(taskId);
        return total;
    }

    /**
     * 设置终止任务日志的阅读时间
     * @param qIn
     * readTime
     * readUserId
     */
    @Override
    public void setReadTime(Map qIn) {
        taskStopDao.setReadTime(qIn);
    }
}
