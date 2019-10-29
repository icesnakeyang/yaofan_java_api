package com.gogoyang.yaofan.utility.common;

import com.gogoyang.yaofan.meta.task.entity.Task;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;

import java.util.Map;

public interface ICommonBusinessService {
    void createUserActLog(Map in) throws Exception;

    UserInfo getUserByToken(String token) throws Exception;

    /**
     * 检查task是否有重复任务
     * true：重复
     * false：不重复
     */
    boolean isDuplicateTask(Task task) throws Exception;
}
