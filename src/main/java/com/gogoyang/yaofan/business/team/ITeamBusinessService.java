package com.gogoyang.yaofan.business.team;

import java.util.Map;

public interface ITeamBusinessService {
    Map createTeam(Map in) throws Exception;

    Map listTeam(Map in) throws Exception;

    Map searchTeam(Map in) throws Exception;

    Map getTeamByTeamId(Map in) throws Exception;

    void applyTeam(Map in) throws Exception;
}
