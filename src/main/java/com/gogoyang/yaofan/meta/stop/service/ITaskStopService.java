package com.gogoyang.yaofan.meta.stop.service;

import com.gogoyang.yaofan.meta.stop.entity.TaskStop;

import java.util.Map;

public interface ITaskStopService {
    /**
     * 创建任务停止日志
     * @param taskStop
     */
    void createTaskStop(TaskStop taskStop);

    /**
     * 读取终止任务记录
     * @param qIn
     * taskId
     * createUserId
     * taskStopId
     * @return
     */
    TaskStop getTaskStop(Map qIn);

    /**
     * 统计未阅读的终止日志
     * @param qIn
     * taskId
     * partyAId
     * partyBId
     * @return
     */
    Integer totalTaskStopUnread(Map qIn);

    /**
     * 统计终止任务日志数
     * @param taskId
     * @return
     */
    Integer totalTaskStop(String taskId);

    /**
     * 设置终止任务日志的阅读时间
     * @param qIn
     * readTime
     * readUserId
     * taskStopId
     */
    void setReadTime(Map qIn);
}
