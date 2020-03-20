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

    /**
     * 统计用户已完成的任务总数
     * @param qIn
     * @return
     * @throws Exception
     */
    Map countUserTaskComplete(Map qIn) throws Exception;

    /**
     * 统计用户进行中的任务总数
     * @param qIn
     * @return
     * @throws Exception
     */
    Map countUserTaskProgress(Map qIn) throws Exception;

    /**
     * 统计一个用户指定时间段内的积分收入和积分支出
     * @param qIn
     * @return
     * @throws Exception
     */
    Map totalPointIn(Map qIn) throws Exception;

    /**
     * 读取任务列表
     * @param qIn
     * @return
     * @throws Exception
     */
    ArrayList<Task> listMyTasksDetail(Map qIn) throws Exception;
}
