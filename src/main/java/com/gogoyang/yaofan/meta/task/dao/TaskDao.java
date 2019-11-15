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
}
