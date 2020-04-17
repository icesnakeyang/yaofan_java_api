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

import java.sql.SQLInput;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelComplete(Map in) throws Exception {
        String token = in.get("token").toString();
        String taskId = in.get("taskId").toString();
        String content = in.get("content").toString();

        /**
         * 1、读取当前用户
         * 2、读取要修改的任务, 检查任务状态是否complete
         * 3、检查用户是否乙方
         * 4、读取complete日志
         * 5、检查complete日志是否未处理
         * 6、处理complete日志为cancel
         * 7、修改任务状态为progress
         */

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Task task = iCommonBusinessService.getTaskByTaskId(taskId);

        if (!task.getStatus().equals(GogoStatus.COMPLETE.toString())) {
            throw new Exception("20008");
        }

        if (!task.getPartyBId().equals(userInfo.getUserId())) {
            throw new Exception("20009");
        }

        Map qIn = new HashMap();
        qIn.put("taskId", task.getTaskId());
        ArrayList<TaskComplete> taskCompletes = iTaskCompleteService.listTaskComplete(qIn);
        TaskComplete taskComplete = null;
        for (int i = 0; i < taskCompletes.size(); i++) {
            if (taskCompletes.get(i).getProcessResult() == null) {
                taskComplete = taskCompletes.get(i);
            }
        }
        if (taskComplete == null) {
            throw new Exception("20010");
        }

        taskComplete.setProcessRemark(content);
        taskComplete.setProcessResult(GogoStatus.CANCEL.toString());
        taskComplete.setProcessTime(new Date());
        taskComplete.setProcessUserId(userInfo.getUserId());
        iTaskCompleteService.processResult(taskComplete);

        task.setStatus(GogoStatus.PROGRESS.toString());
        iTaskService.updateTaskStatus(task);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void rejectComplete(Map in) throws Exception {
        String token=in.get("token").toString();
        String taskId=in.get("taskId").toString();
        String content=in.get("content").toString();

        UserInfo userInfo=iCommonBusinessService.getUserByToken(token);

        Task task=iCommonBusinessService.getTaskByTaskId(taskId);

        if(!task.getCreateUserId().equals(userInfo.getUserId())){
            //不是甲方，不能拒绝
            throw new Exception("20011");
        }

        TaskComplete taskComplete=iTaskCompleteService.getTaskCompleteUnProcess(taskId);
        if(taskComplete==null){
            throw new Exception("20010");
        }
        taskComplete.setProcessRemark(content);
        taskComplete.setProcessResult(GogoStatus.REJECT.toString());
        taskComplete.setProcessTime(new Date());
        taskComplete.setProcessUserId(userInfo.getUserId());
        iTaskCompleteService.processResult(taskComplete);

        task.setStatus(GogoStatus.PROGRESS.toString());
        iTaskService.updateTaskStatus(task);
    }

    /**
     * 同意任务验收
     * @param in
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void acceptComplete(Map in) throws Exception {
        String token=in.get("token").toString();
        String taskId=in.get("taskId").toString();
        String content=in.get("content").toString();

        UserInfo userInfo=iCommonBusinessService.getUserByToken(token);

        Task task=iCommonBusinessService.getTaskByTaskId(taskId);

        if(!task.getCreateUserId().equals(userInfo.getUserId())){
            //不是甲方，不能验收
            throw new Exception("20019");
        }

        TaskComplete taskComplete=iTaskCompleteService.getTaskCompleteUnProcess(taskId);
        if(taskComplete==null){
            throw new Exception("20010");
        }
        taskComplete.setProcessRemark(content);
        taskComplete.setProcessResult(GogoStatus.ACCEPT.toString());
        taskComplete.setProcessTime(new Date());
        taskComplete.setProcessUserId(userInfo.getUserId());
        iTaskCompleteService.processResult(taskComplete);

        task.setStatus(GogoStatus.ACCEPT.toString());
        iTaskService.updateTaskStatus(task);
    }
}
