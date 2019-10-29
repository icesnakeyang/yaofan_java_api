package com.gogoyang.yaofan.meta.task.service;

import com.gogoyang.yaofan.meta.task.entity.Task;

import java.util.ArrayList;
import java.util.Map;

public interface ITaskService {
    void createTask(Task task) throws Exception;

    ArrayList<Task> listTasks(Map qIn) throws Exception;

    Task getTaskByTaskId(String taskAId) throws Exception;

    Integer totalTaskDuplicate(Map qIn) throws Exception;
}
