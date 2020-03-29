package com.gogoyang.yaofan.business.team;

import java.util.Map;

public interface ITeamBusinessService {
    Map createTeam(Map in) throws Exception;

    Map listMyTeam(Map in) throws Exception;

    Map searchTeam(Map in) throws Exception;

    Map getTeamByTeamId(Map in) throws Exception;

    void applyTeam(Map in) throws Exception;

    /**
     * 读取我申请的团队日志
     * 包括已处理和未处理的
     * @param in
     * @return
     * @throws Exception
     */
    Map listTeamApplyLogMyApply(Map in) throws Exception;

    Map listTeamApplyLogApplyUser(Map in) throws Exception;

    Map getTeamApplyLog(Map in) throws Exception;

    void rejectApplyTeam(Map in) throws Exception;

    void agreeApplyTeam(Map in) throws Exception;

    void updateMyTeam(Map in) throws Exception;

    void deleteMyTeam(Map in) throws Exception;

    /**
     * 统计当前用户未读的团队日志
     * 1、统计未读的加入我的团队申请
     * 2、统计未读的已处理的我加入的团队申请
     * @param in
     * @return
     * @throws Exception
     */
    Map totalMyTeamLogUnread(Map in) throws Exception;

    void cancelTeamApplyLog(Map in) throws Exception;
}
