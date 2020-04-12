package com.gogoyang.yaofan.meta.task.service;

import com.gogoyang.yaofan.meta.task.entity.Task;

import java.util.ArrayList;
import java.util.Map;

public interface ITaskService {
    void createTask(Task task) throws Exception;

    ArrayList<Task> listBiddingTasks(Map qIn) throws Exception;

    Task getTaskByTaskId(String taskId) throws Exception;

    Integer totalTaskDuplicate(Map qIn) throws Exception;

    /**
     * 修改任务为抢单成功
     * @param taskId
     * @param userId
     * @throws Exception
     */
    void updateTaskDeal(String taskId, String userId) throws Exception;

    /**
     * 读取我的任务（同时包括我是甲方或乙方）不包括详情
     * 按任务完成时间排序
     * @param qIn
     * userId
     * status
     * @return
     */
    ArrayList<Task> listMyTasks(Map qIn) throws Exception;

    void updateTaskStatus(Task task) throws Exception;

    /**
     * 统计用户的任务总数（同时包括甲方任务和乙方任务）
     * @param qIn
     * userId
     * status（可选）
     * @return
     * @throws Exception
     */
    Integer totalUserTask(Map qIn) throws Exception;

    /**
     * 统计我的任务总是（我是甲方或者我是乙方）
     * @param qIn
     * partyAId
     * partyBId
     * status
     * @return
     */
    Integer totalMyTasksPartyAOrB(Map qIn);

    /**
     * 统计一个用户指定时间段内的积分收入和积分支出
     * @param qIn
     * @return
     * @throws Exception
     */
    Map totalPointIn(Map qIn) throws Exception;

    /**
     * 读取我的任务（同时包括我是甲方或乙方）包括详情
     * @param qIn
     * userId
     * offset
     * size
     * @return
     * @throws Exception
     */
    ArrayList<Task> listMyTasksDetail(Map qIn) throws Exception;

    /**
     * 读取我的任务（我是甲方或者我是乙方）包括详情
     * @param qIn
     * partyAId
     * partyBId
     * offset
     * size
     * @return
     */
    ArrayList<Task> listMyTasksDetailPartyAOrB(Map qIn);

    void updateTask(Task task) throws Exception;

    /**
     * 查询一组团队集合里的等待匹配任务
     * @param qIn
     * teamList
     * @return
     */
    ArrayList<Task> listTaskGrabbingTeam(Map qIn);
}
