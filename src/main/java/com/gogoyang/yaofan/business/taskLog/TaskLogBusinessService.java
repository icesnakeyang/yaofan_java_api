package com.gogoyang.yaofan.business.taskLog;

import com.gogoyang.yaofan.meta.task.entity.Task;
import com.gogoyang.yaofan.meta.taskLog.entity.TaskLog;
import com.gogoyang.yaofan.meta.taskLog.service.ITaskLogService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.utility.GogoTools;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

    /**
     * 创建任务日志
     *
     * @param in
     * @throws Exception
     */
    @Override
    public void createTaskLog(Map in) throws Exception {
        String token = in.get("token").toString();
        String taskId = in.get("taskId").toString();
        String content = in.get("content").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Task task = iCommonBusinessService.getTaskByTaskId(taskId);

        iCommonBusinessService.checkTaskMember(userInfo, task);

        TaskLog taskLog = new TaskLog();
        taskLog.setContent(content);
        taskLog.setCreateTime(new Date());
        taskLog.setCreateUserId(userInfo.getUserId());
        taskLog.setTaskId(taskId);
        taskLog.setTaskLogId(GogoTools.UUID().toString());

        iTaskLogService.createTaskLog(taskLog);
    }

    @Override
    public Map listTaskLog(Map in) throws Exception {
        String token = in.get("token").toString();
        String taskId = in.get("taskId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Task task = iCommonBusinessService.getTaskByTaskId(taskId);

        iCommonBusinessService.checkTaskMember(userInfo, task);

        Map qIn = new HashMap();
        qIn.put("taskId", taskId);
        ArrayList<TaskLog> taskLogs = iTaskLogService.listTaskLog(qIn);
        Map out = new HashMap();
        out.put("taskLogs", taskLogs);

        /**
         * 设置日志的阅读时间
         * 如果当前用户是甲方，或者乙方，设置阅读时间
         */
        if (userInfo.getUserId().equals(task.getPartyBId()) ||
                userInfo.getUserId().equals(task.getCreateUserId())) {
            Map qIn2 = new HashMap();
            qIn2.put("readTime", new Date());
            qIn2.put("taskId", taskId);
            qIn2.put("readUserId", userInfo.getUserId());
            iTaskLogService.setTaskLogReadTime(qIn2);
        }

        return out;
    }

    /**
     * 删除任务日志
     *
     * @param in
     * @throws Exception
     */
    @Override
    public void deleteTaskLog(Map in) throws Exception {
        String token = in.get("token").toString();
        String taskLogId = in.get("taskLogId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        TaskLog taskLog = iTaskLogService.getTaskLogByLogId(taskLogId);
        if (!taskLog.getCreateUserId().equals(userInfo.getUserId())) {
            //只能删除自己创建的日志
            throw new Exception("20018");
        }

        iTaskLogService.deleteTaskLog(taskLogId);
    }
}
