package com.gogoyang.yaofan.business.task;

import org.omg.CORBA.WCharSeqHelper;

import java.util.Map;

public interface ITaskBusinessService {
    void createTask(Map in) throws Exception;

    /**
     * 查询我可以抢单的任务
     * @param in
     * @return
     * @throws Exception
     */
    Map listBiddingTasks(Map in) throws Exception;

    Map getTaskByTaskId(Map in) throws Exception;

    /**
     * 查询我的任务
     * @param in
     * @return
     * @throws Exception
     */
    Map listMyTasks(Map in) throws Exception;

    void grab(Map in) throws Exception;

    /**
     * 读取我的任务列表，包括任务详情
     * @param in
     * @return
     * @throws Exception
     */
    Map listMyTasksDetail(Map in) throws Exception;

    void updateTask(Map in) throws Exception;

    Map listTaskGrabbingTeam(Map in) throws Exception;
}
