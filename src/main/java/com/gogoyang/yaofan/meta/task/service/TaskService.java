package com.gogoyang.yaofan.meta.task.service;

import com.gogoyang.yaofan.meta.task.dao.TaskDao;
import com.gogoyang.yaofan.meta.task.entity.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class TaskService implements ITaskService {
    private final TaskDao taskDao;

    public TaskService(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public void createTask(Task task) throws Exception {
        taskDao.createTask(task);
    }

    @Override
    public ArrayList<Task> listTasks(Map qIn) throws Exception {
        ArrayList<Task> tasks = taskDao.listTasks(qIn);
        return tasks;
    }

    @Override
    public Task getTaskByTaskId(String taskId) throws Exception {
        Task task = taskDao.getTaskByTaskId(taskId);
        return task;
    }

    @Override
    public Integer totalTaskDuplicate(Map qIn) throws Exception {
        Integer total = taskDao.totalTaskDuplicate(qIn);
        return total;
    }
}
