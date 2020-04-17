package com.gogoyang.yaofan.meta.taskLog.service;

import com.gogoyang.yaofan.meta.taskLog.entity.TaskLog;

import java.util.ArrayList;
import java.util.Map;

public interface ITaskLogService {
    void createTaskLog(TaskLog taskLog) throws Exception;
    ArrayList<TaskLog> listTaskLog(Map qIn) throws Exception;
    void setTaskLogReadTime(Map qIn) throws Exception;

    Integer totalTaskLog(String taskId) throws Exception;
    Integer totalTaskLogUnread(String taskId, String readUserId) throws Exception;

    /**
     * 删除任务日志
     * @param taskLogId
     */
    void deleteTaskLog(String taskLogId);

    /**
     * 查询任务日志
     * @param taskLogId
     * @return
     */
    TaskLog getTaskLogByLogId(String taskLogId);
}
