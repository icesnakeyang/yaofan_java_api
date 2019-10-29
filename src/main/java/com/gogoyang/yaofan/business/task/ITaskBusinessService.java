package com.gogoyang.yaofan.business.task;

import java.util.Map;

public interface ITaskBusinessService {
    void createTask(Map in) throws Exception;

    Map listBiddingTasks(Map in) throws Exception;

    Map getTaskByTaskId(Map in) throws Exception;
}
