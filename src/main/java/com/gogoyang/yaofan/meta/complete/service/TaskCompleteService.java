package com.gogoyang.yaofan.meta.complete.service;

import com.gogoyang.yaofan.meta.complete.dao.TaskCompleteDao;
import com.gogoyang.yaofan.meta.complete.entity.TaskComplete;
import org.springframework.stereotype.Service;

@Service
public class TaskCompleteService implements ITaskCompleteService {
    private final TaskCompleteDao taskCompleteDao;

    public TaskCompleteService(TaskCompleteDao taskCompleteDao) {
        this.taskCompleteDao = taskCompleteDao;
    }

    @Override
    public void createTaskComplete(TaskComplete taskComplete) {
        taskCompleteDao.createTaskComplete(taskComplete);
    }
}
