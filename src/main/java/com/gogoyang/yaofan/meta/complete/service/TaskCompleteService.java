package com.gogoyang.yaofan.meta.complete.service;

import com.gogoyang.yaofan.meta.complete.dao.TaskCompleteDao;
import com.gogoyang.yaofan.meta.complete.entity.TaskComplete;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class TaskCompleteService implements ITaskCompleteService {
    private final TaskCompleteDao taskCompleteDao;

    public TaskCompleteService(TaskCompleteDao taskCompleteDao) {
        this.taskCompleteDao = taskCompleteDao;
    }

    @Override
    public void createTaskComplete(TaskComplete taskComplete) throws Exception {
        taskCompleteDao.createTaskComplete(taskComplete);
    }

    /**
     * 读取完成日志列表
     * @param qIn
     * taskId
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<TaskComplete> listTaskComplete(Map qIn) throws Exception {
        ArrayList<TaskComplete> taskCompletes = taskCompleteDao.listTaskComplete(qIn);
        return taskCompletes;
    }

    @Override
    public Integer totalCompleteUnread(String taskId, String userId) throws Exception {
        Map qIn = new HashMap();
        qIn.put("taskId", taskId);
        qIn.put("userId", userId);
        Integer total = taskCompleteDao.totalCompleteUnread(qIn);
        return total;
    }

    @Override
    public void setTaskCompleteReadTime(Map qIn) throws Exception {
        taskCompleteDao.setTaskCompleteReadTime(qIn);
    }

    @Override
    public Integer totalTaskComplete(String taskId) throws Exception {
        Map qIn=new HashMap();
        qIn.put("taskId", taskId);
        Integer total = taskCompleteDao.totalTaskComplete(qIn);
        return total;
    }

    /**
     * 根据taskCompleteId读取任务完成日志
     * @param taskCompleteId
     * @return
     */
    @Override
    public TaskComplete getTaskCompleteTiny(String taskCompleteId) {
        TaskComplete taskComplete=taskCompleteDao.getTaskCompleteTiny(taskCompleteId);
        return taskComplete;
    }

    /**
     * 处理完成日志结果
     * @param taskComplete
     * @throws Exception
     */
    @Override
    public void processResult(TaskComplete taskComplete) throws Exception {
        taskCompleteDao.processResult(taskComplete);
    }
}
