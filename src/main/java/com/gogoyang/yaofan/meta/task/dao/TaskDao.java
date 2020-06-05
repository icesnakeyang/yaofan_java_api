package com.gogoyang.yaofan.meta.task.dao;

import com.gogoyang.yaofan.meta.task.entity.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface TaskDao {

    void createTask(Task task);

    /**
     * 读取所有我可以抢单的任务，包括所有我加入的团队的任务
     * @param qIn
     * userId
     * teamList
     * @return
     */
    ArrayList<Task> listBiddingTasks(Map qIn);

    /**
     * 通过taskId读取一个任务详情
     * @param taskId
     * @return
     */
    Task getTaskByTaskId(String taskId);

    /**
     * 统计重复的任务数量
     * @param qIn
     * @return
     */
    Integer totalTaskDuplicate(Map qIn);

    /**
     * 修改任务为抢单成功
     *
     * @param qIn
     */
    void updateTaskDeal(Map qIn);

    /**
     * 读取我的任务（同时包括我是甲方或乙方）不包括详情
     * 按任务完成时间排序
     * @param qIn
     * userId
     * status
     * @return
     */
    ArrayList<Task> listMyTasks(Map qIn);

    /**
     * 直接根据taskId修改任务状态
     * @param task
     */
    void updateTaskStatus(Task task);

    /**
     * 统计用户的任务总数（同时包括甲方任务和乙方任务）
     * @param qIn
     * userId
     * status（可选）
     * @return
     */
    Integer totalUserTask(Map qIn);

    /**
     * 统计一个用户指定时间段内的积分收入和积分支出
     * @param qIn
     * @return
     */
    Map totalPointIn(Map qIn);

    /**
     * 读取我的任务（同时包括我是甲方或乙方）包括详情
     * @param qIn
     * userId
     * offset
     * size
     * @return
     */
    ArrayList<Task> listMyTasksDetail(Map qIn);

    /**
     * 读取我的任务（我是甲方或者我是乙方）包括详情
     * @param qIn
     * partyAId
     * partyBId
     * history
     * offset
     * size
     * @return
     */
    ArrayList<Task> listMyTasksDetailPartyAOrB(Map qIn);

    /**
     * 增量修改任务
     * @param task
     * point
     * title
     * detail
     * endTime
     * teamId
     */
    void updateTask(Task task);

    /**
     * 查询一组团队集合里的等待匹配任务
     * @param qIn
     * teamList
     * @return
     */
    ArrayList<Task> listTaskGrabbingTeam(Map qIn);

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
     * 删除一个任务
     * @param taskId
     */
    void deleteTask(String taskId);

    /**
     * 读取多个任务列表
     * @param qIn
     * teamList
     * @return
     */
    ArrayList<Task> listTask(Map qIn);

    Integer totalTask(Map qIn);
}
