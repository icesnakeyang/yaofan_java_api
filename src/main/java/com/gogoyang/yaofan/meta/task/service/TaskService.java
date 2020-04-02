package com.gogoyang.yaofan.meta.task.service;

import com.gogoyang.yaofan.meta.task.dao.TaskDao;
import com.gogoyang.yaofan.meta.task.entity.Task;
import com.gogoyang.yaofan.utility.GogoStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
    public ArrayList<Task> listBiddingTasks(Map qIn) throws Exception {
        ArrayList<Task> tasks = taskDao.listBiddingTasks(qIn);
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

    /**
     * 修改任务为抢单成功
     *
     * @param taskId
     * @param userId
     * @throws Exception
     */
    @Override
    public void updateTaskDeal(String taskId, String userId) throws Exception {
        Map qIn = new HashMap();
        qIn.put("partyBId", userId);
        qIn.put("contractTime", new Date());
        qIn.put("status", GogoStatus.PROGRESS);
        qIn.put("taskId", taskId);
        taskDao.updateTaskDeal(qIn);
    }

    @Override
    public ArrayList<Task> listMyTasks(Map qIn) throws Exception {
        ArrayList<Task> tasks = taskDao.listMyTasks(qIn);
        return tasks;
    }

    @Override
    public void updateTaskStatus(Task task) throws Exception {
        taskDao.updateTaskStatus(task);
    }

    @Override
    public Map countUserTask(Map qIn) throws Exception {
        Map out = taskDao.countUserTask(qIn);
        return out;
    }

    /**
     * 统计用户已完成的任务总数
     *
     * @param qIn
     * @return
     * @throws Exception
     */
    @Override
    public Map countUserTaskComplete(Map qIn) throws Exception {
        Map out = taskDao.countUserTaskComplete(qIn);
        return out;
    }

    /**
     * 统计用户进行中的任务总数
     *
     * @param qIn
     * @return
     * @throws Exception
     */
    @Override
    public Map countUserTaskProgress(Map qIn) throws Exception {
        Map out = taskDao.countUserTaskProgress(qIn);
        return out;
    }

    /**
     * 统计一个用户指定时间段内的积分收入和积分支出
     *
     * @param qIn
     * @return
     * @throws Exception
     */
    @Override
    public Map totalPointIn(Map qIn) throws Exception {
        return taskDao.totalPointIn(qIn);
    }

    /**
     * 读取我的任务（同时包括我是甲方或乙方）包括详情
     * @param qIn
     * userId
     * offset
     * size
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<Task> listMyTasksDetail(Map qIn) throws Exception {
        ArrayList<Task> tasks = taskDao.listMyTasksDetail(qIn);
        return tasks;
    }

    /**
     * 读取我的任务（我是甲方或者我是乙方）包括详情
     * @param qIn
     * partyAId
     * partyBId
     * offset
     * size
     * @return
     */
    @Override
    public ArrayList<Task> listMyTasksDetailPartyAOrB(Map qIn) {
        ArrayList<Task> tasks=taskDao.listMyTasksDetailPartyAOrB(qIn);
        return tasks;
    }

    @Override
    public void updateTask(Task task) throws Exception {
        taskDao.updateTask(task);
    }

    /**
     * 查询一组团队集合里的等待匹配任务
     *
     * @param qIn
     * teamList
     * @return
     */
    @Override
    public ArrayList<Task> listTaskGrabbingTeam(Map qIn) {
        ArrayList<Task> tasks = taskDao.listTaskGrabbingTeam(qIn);
        return tasks;
    }
}
