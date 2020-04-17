package com.gogoyang.yaofan.business.stop;

import com.gogoyang.yaofan.meta.point.entity.PointLedger;
import com.gogoyang.yaofan.meta.point.service.IPointService;
import com.gogoyang.yaofan.meta.stop.entity.TaskStop;
import com.gogoyang.yaofan.meta.stop.service.ITaskStopService;
import com.gogoyang.yaofan.meta.task.entity.Task;
import com.gogoyang.yaofan.meta.task.service.ITaskService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.utility.GogoActType;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.GogoTools;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TaskStopBusinessService implements ITaskStopBusinessService {
    private final ITaskStopService iTaskStopService;
    private final ICommonBusinessService iCommonBusinessService;
    private final ITaskService iTaskService;
    private final IPointService iPointService;

    public TaskStopBusinessService(ITaskStopService iTaskStopService,
                                   ICommonBusinessService iCommonBusinessService,
                                   ITaskService iTaskService,
                                   IPointService iPointService) {
        this.iTaskStopService = iTaskStopService;
        this.iCommonBusinessService = iCommonBusinessService;
        this.iTaskService = iTaskService;
        this.iPointService = iPointService;
    }

    /**
     * 甲方用户终止任务
     *
     * @param in
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void stopTask(Map in) throws Exception {
        String token = in.get("token").toString();
        String taskId = in.get("taskId").toString();
        String remark = in.get("remark").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Task task = iCommonBusinessService.getTaskByTaskId(taskId);

        if (!task.getCreateUserId().equals(userInfo.getUserId())) {
            //当前用户不是甲方，不能终止任务
            throw new Exception("20011");
        }

        if (task.getStatus().equals(GogoStatus.ACCEPT.toString())) {
            //不能终止已经验收的任务
            throw new Exception("20012");
        }

        if(task.getStatus().equals(GogoStatus.GRABBING.toString())){
            //不能终止等待抢单的任务
            throw new Exception("20017");
        }

        //创建taskStop日志
        TaskStop taskStop = new TaskStop();
        taskStop.setCreateTime(new Date());
        taskStop.setCreateUserId(userInfo.getUserId());
        taskStop.setRemark(remark);
        taskStop.setTaskId(taskId);
        taskStop.setTaskStopId(GogoTools.UUID().toString());
        iTaskStopService.createTaskStop(taskStop);

        //修改任务状态为终止Stop
        task.setStatus(GogoStatus.STOP.toString());
        iTaskService.updateTaskStatus(task);

        /**
         * 终止任务后，把point收回
         * 1、增加甲方的pointIn
         * 2、乙方pointOut
         */
        PointLedger pointLedger=new PointLedger();
        pointLedger.setUserId(task.getCreateUserId());
        pointLedger.setTaskId(taskId);
        pointLedger.setPointIn(task.getPoint());
        pointLedger.setCreateTime(new Date());
        pointLedger.setActType(GogoActType.STOP_TASK.toString());
        pointLedger.setPointLedgerId(GogoTools.UUID().toString());
        iPointService.createPointLedger(pointLedger);

        pointLedger.setUserId(task.getPartyBId());
        pointLedger.setPointIn(null);
        pointLedger.setPointOut(task.getPoint());
        pointLedger.setPointLedgerId(GogoTools.UUID().toString());
        iPointService.createPointLedger(pointLedger);
    }

    /**
     * 读取终止任务日志详情
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map getTaskStop(Map in) throws Exception {
        String token = in.get("token").toString();
        String taskId = (String) in.get("taskId");

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);
        Map qIn = new HashMap();
        qIn.put("taskId", taskId);
        qIn.put("partyBId", userInfo.getUserId());
        TaskStop taskStop = iTaskStopService.getTaskStop(qIn);

        Map out = new HashMap();
        out.put("taskStop", taskStop);

        //如果当前用户是乙方，设置阅读时间
        if(taskStop!=null && taskStop.getReadTime()==null){
            Task task=iCommonBusinessService.getTaskByTaskId(taskId);
            if(task.getPartyBId().equals(userInfo.getUserId())){
                //乙方，设置阅读时间
                qIn=new HashMap();
                qIn.put("taskStopId", taskStop.getTaskStopId());
                qIn.put("readTime", new Date());
                qIn.put("readUserId", userInfo.getUserId());
                iTaskStopService.setReadTime(qIn);
            }
        }

        return out;
    }
}
