package com.gogoyang.yaofan.business.volunteer;

import java.util.Map;

public interface IVolunteerBusinessService {
    void createVolunteerTask(Map in) throws Exception;

    Map listVolunteerTask(Map in) throws Exception;

    Map getVolunteerTask(Map in) throws Exception;
}
