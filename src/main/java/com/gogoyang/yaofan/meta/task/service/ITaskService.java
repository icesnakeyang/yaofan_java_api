package com.gogoyang.yaofan.meta.task.service;

import com.gogoyang.yaofan.meta.task.entity.Task;

import java.util.ArrayList;
import java.util.Map;

public interface ITaskService {
    void createTask(Task task) throws Exception;

    ArrayList<Task> listBiddingTasks(Map qIn) throws Exception;

    Task getTaskByTaskId(String taskAId) throws Exception;

    Integer totalTaskDuplicate(Map qIn) throws Exception;

    /**
     * 修改任务为抢单成功
     * @param taskId
     * @param userId
     * @throws Exception
     */
    void updateTaskDeal(String taskId, String userId) throws Exception;

    ArrayList<Task> listMyTasks(Map qIn) throws Exception;

    void updateTaskStatus(Task task) throws Exception;

    /**
     * 统计用户的任务总数
     * @param qIn
     * @return
     * @throws Exception
     */
    Map countUserTask(Map qIn) throws Exception;
}
