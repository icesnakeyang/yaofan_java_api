package com.gogoyang.yaofan.business.volunteer;

import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.meta.volunteer.IVolunteerService;
import com.gogoyang.yaofan.meta.volunteer.apply.entity.VolunteerApply;
import com.gogoyang.yaofan.meta.volunteer.task.entity.VolunteerTask;
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
public class VolunteerBusinessService implements IVolunteerBusinessService {
    private final IVolunteerService iVolunteerService;
    private final ICommonBusinessService iCommonBusinessService;

    public VolunteerBusinessService(IVolunteerService iVolunteerService,
                                    ICommonBusinessService iCommonBusinessService) {
        this.iVolunteerService = iVolunteerService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    /**
     * 创建义工任务
     *
     * @param in
     * @throws Exception
     */
    @Override
    public void createVolunteerTask(Map in) throws Exception {
        String token = in.get("token").toString();
        String content = in.get("content").toString();
        String pid = (String) in.get("pid");
        Date startTime = (Date) in.get("startTime");
        String title = in.get("title").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        VolunteerTask volunteerTask = new VolunteerTask();
        volunteerTask.setContent(content);
        volunteerTask.setCreateTime(new Date());
        volunteerTask.setCreateUserId(userInfo.getUserId());
        volunteerTask.setPid(pid);
        volunteerTask.setStartTime(startTime);
        volunteerTask.setTitle(title);
        volunteerTask.setVolunteerTaskId(GogoTools.UUID().toString());
        volunteerTask.setStatus(GogoStatus.ACTIVE.toString());

        iVolunteerService.createVolunteerTask(volunteerTask);
    }

    @Override
    public Map listVolunteerTask(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        Integer offset = (pageIndex - 1) * pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        qIn.put("status", GogoStatus.ACTIVE);
        ArrayList<VolunteerTask> volunteerTasks = iVolunteerService.listVolunteerTask(qIn);

        Map out = new HashMap();
        out.put("volunteerTasks", volunteerTasks);

        return out;
    }

    @Override
    public Map getVolunteerTask(Map in) throws Exception {
        String token = in.get("token").toString();
        String volunteerTaskId = in.get("volunteerTaskId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        VolunteerTask volunteerTask = iVolunteerService.getVolunteerTaskDetail(volunteerTaskId);

        Map out = new HashMap();
        out.put("volunteerTask", volunteerTask);

        /**
         * 检查当前用户是否已经通过了该任务的义工
         */
        Map qIn = new HashMap();
        qIn.put("applyUserId", userInfo.getUserId());
        qIn.put("volunteerTaskId", volunteerTaskId);
        ArrayList<VolunteerApply> volunteerApplies = iVolunteerService.listVolunteerApply(qIn);
        int cc = 0;
        if (volunteerApplies.size() > 0) {
            //当前用户已经申请了该义工任务
            for (int i = 0; i < volunteerApplies.size(); i++) {
                VolunteerApply apply = volunteerApplies.get(i);
                if (apply.getProcessResult() == null) {
                    //未处理
                    cc++;
                    out.put("isPending", true);
                } else {
                    if (apply.getProcessResult().equals(GogoStatus.AGREE.toString())) {
                        //已通过
                        cc++;
                        out.put("isAgree", true);
                    }
                }
            }
        }
        if (cc == 0) {
            //当前用户可以申请
            out.put("canApply", true);
        }

        return out;
    }

    /**
     * 用户报名申请义工任务
     *
     * @param in
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void applyVolunteerTask(Map in) throws Exception {
        String token = in.get("token").toString();
        String volunteerTaskId = in.get("volunteerTaskId").toString();
        String remark = in.get("remark").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        VolunteerTask volunteerTask = iVolunteerService.getVolunteerTaskTiny(volunteerTaskId);

        if (!volunteerTask.getStatus().equals(GogoStatus.ACTIVE.toString())) {
            throw new Exception("20021");
        }

        if (volunteerTask.getCreateUserId().equals(userInfo.getUserId())) {
            throw new Exception("20023");
        }

        VolunteerApply volunteerApply = new VolunteerApply();
        volunteerApply.setApplyUserId(userInfo.getUserId());
        volunteerApply.setCreateTime(new Date());
        volunteerApply.setRemark(remark);
        volunteerApply.setVolunteerApplyId(GogoTools.UUID().toString());
        volunteerApply.setVolunteerTaskId(volunteerTaskId);
        iVolunteerService.createVolunteerApply(volunteerApply);
    }

    @Override
    public Map listMyVolunteerTaskApply(Map in) throws Exception {
        String token = in.get("token").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);
        Map qIn = new HashMap();
        qIn.put("createUserId", userInfo.getUserId());
        ArrayList<VolunteerApply> volunteerApplies = iVolunteerService.listVolunteerApply(qIn);

        Map out = new HashMap();
        out.put("volunteerApplies", volunteerApplies);

        return out;
    }

    @Override
    public Map getVolunteerApply(Map in) throws Exception {
        String token = in.get("token").toString();
        String volunteerApplyId = in.get("volunteerApplyId").toString();

        VolunteerApply volunteerApply = iVolunteerService.getVolunteerApply(volunteerApplyId);

        Map out = new HashMap();
        out.put("volunteerApply", volunteerApply);

        return out;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void rejectVolunteerApply(Map in) throws Exception {
        String token = in.get("token").toString();
        String volunteerApplyId = in.get("volunteerApplyId").toString();
        String remark = in.get("remark").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        VolunteerApply volunteerApply = iVolunteerService.getVolunteerApply(volunteerApplyId);

        if (!volunteerApply.getTaskCreateUserId().equals(userInfo.getUserId())) {
            throw new Exception("20022");
        }

        Map qIn = new HashMap();
        qIn.put("processResult", GogoStatus.REJECT.toString());
        qIn.put("processTime", new Date());
        qIn.put("processRemark", remark);
        qIn.put("processUserId", userInfo.getUserId());
        qIn.put("volunteerApplyId", volunteerApplyId);
        iVolunteerService.processVolunteerApply(qIn);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void agreeVolunteerApply(Map in) throws Exception {
        String token = in.get("token").toString();
        String volunteerApplyId = in.get("volunteerApplyId").toString();
        String remark = in.get("remark").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        VolunteerApply volunteerApply = iVolunteerService.getVolunteerApply(volunteerApplyId);

        if (!volunteerApply.getTaskCreateUserId().equals(userInfo.getUserId())) {
            throw new Exception("20022");
        }

        Map qIn = new HashMap();
        qIn.put("processResult", GogoStatus.AGREE.toString());
        qIn.put("processTime", new Date());
        qIn.put("processRemark", remark);
        qIn.put("processUserId", userInfo.getUserId());
        qIn.put("volunteerApplyId", volunteerApplyId);
        iVolunteerService.processVolunteerApply(qIn);
    }

    @Override
    public Map listMyVolunteerTaskApplyJoin(Map in) throws Exception {
        String token = in.get("token").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("applyUserId", userInfo.getUserId());
        ArrayList<VolunteerApply> volunteerApplies = iVolunteerService.listVolunteerApply(qIn);

        Map out = new HashMap();
        out.put("volunteerApplies", volunteerApplies);

        return out;
    }

    @Override
    public Map listMyVolunteerAgree(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("createUserId", userInfo.getUserId());
        Integer offset = (pageIndex - 1) * pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<VolunteerApply> volunteers = iVolunteerService.listMyVolunteerAgree(qIn);

        Map out = new HashMap();
        out.put("volunteers", volunteers);

        return out;
    }
}
