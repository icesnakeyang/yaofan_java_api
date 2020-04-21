package com.gogoyang.yaofan.business.volunteer;

import java.util.Map;

public interface IVolunteerBusinessService {
    void createVolunteerTask(Map in) throws Exception;

    Map listVolunteerTask(Map in) throws Exception;

    Map getVolunteerTask(Map in) throws Exception;

    void applyVolunteerTask(Map in) throws Exception;

    Map listMyVolunteerTaskApply(Map in) throws Exception;

    Map getVolunteerApply(Map in) throws Exception;

    void rejectVolunteerApply(Map in) throws Exception;

    void agreeVolunteerApply(Map in) throws Exception;

    Map listMyVolunteerTaskApplyJoin(Map in) throws Exception;

    Map listMyVolunteerAgree(Map in) throws Exception;
}
