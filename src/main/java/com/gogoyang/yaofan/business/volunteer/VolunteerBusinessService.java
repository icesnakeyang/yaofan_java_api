package com.gogoyang.yaofan.business.volunteer;

import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.meta.volunteer.IVolunteerService;
import com.gogoyang.yaofan.meta.volunteer.task.entity.VolunteerTask;
import com.gogoyang.yaofan.utility.GogoTools;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class VolunteerBusinessService implements IVolunteerBusinessService{
    private final IVolunteerService iVolunteerService;
    private final ICommonBusinessService iCommonBusinessService;

    public VolunteerBusinessService(IVolunteerService iVolunteerService,
                                    ICommonBusinessService iCommonBusinessService) {
        this.iVolunteerService = iVolunteerService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    /**
     * 创建义工任务
     * @param in
     * @throws Exception
     */
    @Override
    public void createVolunteerTask(Map in) throws Exception {
        String token=in.get("token").toString();
        String content=in.get("content").toString();
        String pid=(String)in.get("pid");
        Date startTime=(Date)in.get("startTime");
        String title=in.get("title").toString();

        UserInfo userInfo=iCommonBusinessService.getUserByToken(token);

        VolunteerTask volunteerTask=new VolunteerTask();
        volunteerTask.setContent(content);
        volunteerTask.setCreateTime(new Date());
        volunteerTask.setCreateUserId(userInfo.getUserId());
        volunteerTask.setPid(pid);
        volunteerTask.setStartTime(startTime);
        volunteerTask.setTitle(title);
        volunteerTask.setVolunteerTaskId(GogoTools.UUID().toString());

        iVolunteerService.createVolunteerTask(volunteerTask);
    }
}
