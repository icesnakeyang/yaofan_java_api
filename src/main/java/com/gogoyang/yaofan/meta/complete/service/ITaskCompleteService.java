package com.gogoyang.yaofan.meta.complete.service;

import com.gogoyang.yaofan.meta.complete.entity.TaskComplete;

import java.util.ArrayList;
import java.util.Map;

public interface ITaskCompleteService {
    void createTaskComplete(TaskComplete taskComplete) throws Exception;

    ArrayList<TaskComplete> listTaskComplete(Map qIn) throws Exception;

    Integer totalCompleteUnread(String taskId, String userId) throws Exception;

    void setTaskCompleteReadTime(Map qIn) throws Exception;

    Integer totalTaskComplete(String taskId) throws Exception;
}
