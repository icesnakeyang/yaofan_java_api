package com.gogoyang.yaofan.business.complete;

import com.gogoyang.yaofan.meta.complete.entity.TaskComplete;
import com.gogoyang.yaofan.meta.complete.service.ITaskCompleteService;
import com.gogoyang.yaofan.meta.task.entity.Task;
import com.gogoyang.yaofan.meta.task.service.ITaskService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.GogoTools;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CompleteBusinessService implements ICompleteBusinessService {
    private final ITaskCompleteService iTaskCompleteService;
    private final ICommonBusinessService iCommonBusinessService;
    private final ITaskService iTaskService;

    public CompleteBusinessService(ITaskCompleteService iTaskCompleteService,
                                   ICommonBusinessService iCommonBusinessService,
                                   ITaskService iTaskService) {
        this.iTaskCompleteService = iTaskCompleteService;
        this.iCommonBusinessService = iCommonBusinessService;
        this.iTaskService = iTaskService;
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

        //设置task状态为完成
        task.setStatus(GogoStatus.COMPLETE.toString());
        iTaskService.updateTaskStatus(task);
    }

    @Override
    public Map listTaskComplete(Map in) throws Exception {
        String token = in.get("token").toString();
        String taskId = in.get("taskId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);
        Task task = iCommonBusinessService.getTaskByTaskId(taskId);

        iCommonBusinessService.checkTaskMember(userInfo, task);

        Map qIn = new HashMap();
        qIn.put("taskId", taskId);
        ArrayList<TaskComplete> taskCompletes = iTaskCompleteService.listTaskComplete(qIn);

        Map out = new HashMap();
        out.put("taskCompletes", taskCompletes);

        /**
         * 设置阅读时间
         */
        Map qIn2 = new HashMap();
        qIn2.put("readTime", new Date());
        qIn2.put("taskId", taskId);
        qIn2.put("readUserId", userInfo.getUserId());
        iTaskCompleteService.setTaskCompleteReadTime(qIn2);

        return out;
    }
}
