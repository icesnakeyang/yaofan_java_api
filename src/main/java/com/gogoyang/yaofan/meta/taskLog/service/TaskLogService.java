package com.gogoyang.yaofan.meta.taskLog.service;

import com.gogoyang.yaofan.meta.taskLog.dao.TaskLogDao;
import com.gogoyang.yaofan.meta.taskLog.entity.TaskLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    @Override
    public ArrayList<TaskLog> listTaskLog(Map qIn) throws Exception {
        ArrayList<TaskLog> taskLogs = taskLogDao.listTaskLog(qIn);
        return taskLogs;
    }

    @Override
    public void setTaskLogReadTime(Map qIn) throws Exception {
        taskLogDao.setTaskLogReadTime(qIn);
    }

    @Override
    public Integer totalTaskLog(String taskId) throws Exception {
        Map qIn = new HashMap();
        qIn.put("taskId", taskId);
        Integer totalTaskLog = taskLogDao.totalTaskLog(qIn);
        return totalTaskLog;
    }

    @Override
    public Integer totalTaskLogUnread(String taskId, String readUserId) throws Exception {
        Map qIn = new HashMap();
        qIn.put("taskId", taskId);
        qIn.put("readUserId", readUserId);
        Integer totalTaskLogUnread = taskLogDao.totalTaskLogUnread(qIn);
        return totalTaskLogUnread;
    }
}
