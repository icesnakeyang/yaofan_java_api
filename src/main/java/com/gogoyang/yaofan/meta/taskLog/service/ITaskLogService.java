package com.gogoyang.yaofan.meta.taskLog.service;

import com.gogoyang.yaofan.meta.taskLog.entity.TaskLog;

import java.util.ArrayList;
import java.util.Map;

public interface ITaskLogService {
    void createTaskLog(TaskLog taskLog) throws Exception;
    ArrayList<TaskLog> listTaskLog(Map qIn) throws Exception;
    void setTaskLogReadTime(Map qIn) throws Exception;
}
