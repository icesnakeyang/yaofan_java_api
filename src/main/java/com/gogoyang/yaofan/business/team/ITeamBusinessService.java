package com.gogoyang.yaofan.business.team;

import java.util.Map;

public interface ITeamBusinessService {
    Map createTeam(Map in) throws Exception;

    Map listMyTeam(Map in) throws Exception;

    Map searchTeam(Map in) throws Exception;

    Map getTeamByTeamId(Map in) throws Exception;

    void applyTeam(Map in) throws Exception;

    Map listApplyTeam(Map in) throws Exception;

    Map getApplyTeam(Map in) throws Exception;

    void rejectApplyTeam(Map in) throws Exception;

    void agreeApplyTeam(Map in) throws Exception;
}
