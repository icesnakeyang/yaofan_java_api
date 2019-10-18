package com.gogoyang.yaofan.utility.common;

import com.gogoyang.yaofan.utility.GogoStatus;

import java.util.Map;

public interface ICommonBusinessService {
    void logLogin(Map in, GogoStatus failed, String message) throws Exception;
}
