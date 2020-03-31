package com.gogoyang.yaofan.utility.common;

import com.gogoyang.yaofan.meta.task.entity.Task;
import com.gogoyang.yaofan.meta.team.entity.Team;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;

import java.util.Map;

public interface ICommonBusinessService {
    void createUserActLog(Map in) throws Exception;

    UserInfo getUserByToken(String token) throws Exception;

    UserInfo getUserByUserId(String userId) throws Exception;

    /**
     * 检查task是否有重复任务
     * true：重复
     * false：不重复
     */
    boolean isDuplicateTask(Task task) throws Exception;

    Team getTeamById(String teamId) throws Exception;

    Task getTaskByTaskId(String taskId) throws Exception;

    void checkUserTeam(String userId, String taskId) throws Exception;

    /**
     * 检查用户是否甲方或者乙方
     * @param user
     * @param task
     * @throws Exception
     */
    void checkTaskMember(UserInfo user, Task task) throws Exception;
}
