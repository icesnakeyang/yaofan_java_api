package com.gogoyang.yaofan.meta.complete.dao;

import com.gogoyang.yaofan.meta.complete.entity.TaskComplete;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface TaskCompleteDao {
    void createTaskComplete(TaskComplete taskComplete);

    /**
     * 读取完成日志列表
     * @param qIn
     * taskId
     * @return
     */
    ArrayList<TaskComplete> listTaskComplete(Map qIn);

    Integer totalCompleteUnread(Map qIn);

    void setTaskCompleteReadTime(Map qIn);

    Integer totalTaskComplete(Map qIn);

    /**
     * 根据taskCompleteId读取任务完成日志
     * @param taskCompleteId
     * @return
     */
    TaskComplete getTaskCompleteTiny(String taskCompleteId);

    /**
     * 处理完成日志结果
     * @param taskComplete
     */
    void processResult(TaskComplete taskComplete);

    /**
     * 读取没有被处理的任务完成日志
     * @param taskId
     * @return
     */
    TaskComplete getTaskCompleteUnProcess(String taskId);
}
