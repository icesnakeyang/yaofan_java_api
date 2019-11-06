package com.gogoyang.yaofan.business.taskLog;

import com.gogoyang.yaofan.meta.task.entity.Task;
import com.gogoyang.yaofan.meta.taskLog.entity.TaskLog;
import com.gogoyang.yaofan.meta.taskLog.service.ITaskLogService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.utility.GogoTools;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class TaskLogBusinessService implements ITaskLogBusinessService {
    private final ITaskLogService iTaskLogService;
    private final ICommonBusinessService iCommonBusinessService;

    public TaskLogBusinessService(ICommonBusinessService iCommonBusinessService,
                                  ITaskLogService iTaskLogService) {
        this.iCommonBusinessService = iCommonBusinessService;
        this.iTaskLogService = iTaskLogService;
    }

    @Override
    public void createTaskLog(Map in) throws Exception {
        String token = in.get("token").toString();
        String taskId = in.get("taskId").toString();
        String content = in.get("content").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Task task = iCommonBusinessService.getTaskByTaskId(taskId);

        if (!userInfo.getUserId().equals(task.getCreateUserId())) {
            if (!userInfo.getUserId().equals(task.getPartyBId())) {
                //当前用户既不是甲方，也不是乙方，不能创建日志
                throw new Exception("10020");
            }
        }

        TaskLog taskLog = new TaskLog();
        taskLog.setContent(content);
        taskLog.setCreateTime(new Date());
        taskLog.setCreateUserId(userInfo.getUserId());
        taskLog.setTaskId(taskId);
        taskLog.setTaskLogId(GogoTools.UUID().toString());

        iTaskLogService.createTaskLog(taskLog);
    }
}
