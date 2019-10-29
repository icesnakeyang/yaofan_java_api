package com.gogoyang.yaofan.meta.task.dao;

import com.gogoyang.yaofan.meta.task.entity.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface TaskDao {

    void createTask(Task task);

    ArrayList<Task> listTasks(Map qIn);

    Task getTaskByTaskId(String taskId);

    Integer totalTaskDuplicate(Map qIn);
}
