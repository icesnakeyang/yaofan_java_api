package com.gogoyang.yaofan.business.stop;

import java.util.Map;

public interface ITaskStopBusinessService {
    void stopTask(Map in) throws Exception;

    Map getTaskStop(Map in) throws Exception;
}
