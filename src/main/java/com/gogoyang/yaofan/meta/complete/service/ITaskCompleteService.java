package com.gogoyang.yaofan.meta.complete.service;

import com.gogoyang.yaofan.meta.complete.entity.TaskComplete;

import java.util.ArrayList;
import java.util.Map;

public interface ITaskCompleteService {
    void createTaskComplete(TaskComplete taskComplete) throws Exception;

    /**
     * 读取完成日志列表
     * @param qIn
     * taskId
     * @return
     * @throws Exception
     */
    ArrayList<TaskComplete> listTaskComplete(Map qIn) throws Exception;

    Integer totalCompleteUnread(String taskId, String userId) throws Exception;

    void setTaskCompleteReadTime(Map qIn) throws Exception;

    Integer totalTaskComplete(String taskId) throws Exception;

    /**
     * 根据taskCompleteId读取任务完成日志
     * @param taskCompleteId
     * @return
     */
    TaskComplete getTaskCompleteTiny(String taskCompleteId);

    /**
     * 处理完成日志结果
     * @param taskComplete
     * @throws Exception
     */
    void processResult(TaskComplete taskComplete) throws Exception;
}
