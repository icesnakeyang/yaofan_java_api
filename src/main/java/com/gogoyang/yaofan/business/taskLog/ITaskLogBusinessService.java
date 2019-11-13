package com.gogoyang.yaofan.business.taskLog;

import java.util.Map;

public interface ITaskLogBusinessService {
    void createTaskLog(Map in) throws Exception;

    Map listTaskLog(Map in) throws Exception;
}
