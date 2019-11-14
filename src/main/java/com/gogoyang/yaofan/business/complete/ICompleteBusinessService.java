package com.gogoyang.yaofan.business.complete;

import java.util.Map;

public interface ICompleteBusinessService {
    void createComplete(Map in) throws Exception;

    Map listTaskComplete(Map in) throws Exception;
}
