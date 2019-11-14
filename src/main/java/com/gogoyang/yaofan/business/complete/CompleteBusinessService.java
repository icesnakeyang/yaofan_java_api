package com.gogoyang.yaofan.business.complete;

import com.gogoyang.yaofan.meta.complete.entity.TaskComplete;
import com.gogoyang.yaofan.meta.complete.service.ITaskCompleteService;
import com.gogoyang.yaofan.meta.task.entity.Task;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.utility.GogoTools;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
public class CompleteBusinessService implements ICompleteBusinessService {
    private final ITaskCompleteService iTaskCompleteService;
    private final ICommonBusinessService iCommonBusinessService;

    public CompleteBusinessService(ITaskCompleteService iTaskCompleteService,
                                   ICommonBusinessService iCommonBusinessService) {
        this.iTaskCompleteService = iTaskCompleteService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createComplete(Map in) throws Exception {
        String token = in.get("token").toString();
        String taskId = in.get("taskId").toString();
        String content = in.get("content").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);
        Task task = iCommonBusinessService.getTaskByTaskId(taskId);

        iCommonBusinessService.checkTaskMember(userInfo, task);

        TaskComplete taskComplete = new TaskComplete();
        taskComplete.setContent(content);
        taskComplete.setCreateTime(new Date());
        taskComplete.setCreateUserId(userInfo.getUserId());
        taskComplete.setTaskCompleteId(GogoTools.UUID().toString());
        taskComplete.setTaskId(taskId);
        iTaskCompleteService.createTaskComplete(taskComplete);
    }
}
