package com.gogoyang.yaofan.meta.task.dao;

import com.gogoyang.yaofan.meta.task.entity.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface TaskDao {

    void createTask(Task task);

    ArrayList<Task> listBiddingTasks(Map qIn);

    Task getTaskByTaskId(String taskId);

    Integer totalTaskDuplicate(Map qIn);

    /**
     * 修改任务为抢单成功
     *
     * @param qIn
     */
    void updateTaskDeal(Map qIn);

    ArrayList<Task> listMyTasks(Map qIn);

    void updateTaskStatus(Task task);

    /**
     * 统计用户的任务总数
     * @param qIn
     * @return
     */
    Map countUserTask(Map qIn);

    /**
     * 统计用户已完成的任务总数
     * @param qIn
     * @return
     */
    Map countUserTaskComplete(Map qIn);

    /**
     * 统计用户进行中的任务总数
     * @param qIn
     * @return
     */
    Map countUserTaskProgress(Map qIn);

    /**
     * 统计一个用户指定时间段内的积分收入和积分支出
     * @param qIn
     * @return
     */
    Map totalPointIn(Map qIn);

    ArrayList<Task> listMyTasksDetail(Map qIn);

    void updateTask(Task task);
}
