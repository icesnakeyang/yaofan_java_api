package com.gogoyang.yaofan.meta.taskLog.service;

import com.gogoyang.yaofan.meta.taskLog.entity.TaskLog;

public interface ITaskLogService {
    void createTaskLog(TaskLog taskLog) throws Exception;
}
