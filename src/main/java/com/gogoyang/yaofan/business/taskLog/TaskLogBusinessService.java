package com.gogoyang.yaofan.business.taskLog;

import com.gogoyang.yaofan.meta.task.entity.Task;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TaskLogBusinessService implements ITaskLogBusinessService{
    private final ICommonBusinessService iCommonBusinessService;

    public TaskLogBusinessService(ICommonBusinessService iCommonBusinessService) {
        this.iCommonBusinessService = iCommonBusinessService;
    }

    @Override
    public void createTaskLog(Map in) throws Exception {
        String token=in.get("token").toString();
        String taskId=in.get("taskId").toString();

        UserInfo userInfo=iCommonBusinessService.getUserByToken(token);

        Task task=iCommonBusinessService.getTaskByTaskId(taskId);
    }
}
