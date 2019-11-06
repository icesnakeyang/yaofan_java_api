package com.gogoyang.yaofan.meta.taskLog.service;

import com.gogoyang.yaofan.meta.taskLog.dao.TaskLogDao;
import com.gogoyang.yaofan.meta.taskLog.entity.TaskLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskLogService implements ITaskLogService {
    private final TaskLogDao taskLogDao;

    public TaskLogService(TaskLogDao taskLogDao) {
        this.taskLogDao = taskLogDao;
    }

    @Override
    public void createTaskLog(TaskLog taskLog) throws Exception {
        taskLogDao.createTaskLog(taskLog);
    }
}
