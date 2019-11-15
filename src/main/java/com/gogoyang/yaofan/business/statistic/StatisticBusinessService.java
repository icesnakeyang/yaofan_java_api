package com.gogoyang.yaofan.business.statistic;

import com.gogoyang.yaofan.meta.point.service.IPointService;
import com.gogoyang.yaofan.meta.task.service.ITaskService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticBusinessService implements IStatisticBusinessService {
    private final ICommonBusinessService iCommonBusinessService;
    private final IPointService iPointService;
    private final ITaskService iTaskService;

    public StatisticBusinessService(ICommonBusinessService iCommonBusinessService,
                                    IPointService iPointService,
                                    ITaskService iTaskService) {
        this.iCommonBusinessService = iCommonBusinessService;
        this.iPointService = iPointService;
        this.iTaskService = iTaskService;
    }

    @Override
    public Map dashboard(Map in) throws Exception {
        String token = in.get("token").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        /**
         * 当前积分余额，总积分收入，总积分支出
         */
        Map qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        Map data = iPointService.totalUserPoint(qIn);

        Double pointIn = (Double) data.get("total_point_in");
        if (pointIn == null) {
            pointIn = 0.0;
        }
        Double pointOut = (Double) data.get("total_point_out");
        if (pointOut == null) {
            pointOut = 0.0;
        }
        Double point = pointIn - pointOut;

        Map out = new HashMap();
        out.put("currentPoint", point);
        out.put("pointIn", pointIn);
        out.put("pointOut", pointOut);

        /**
         * 用户的任务总数
         */
        qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        Map taskMap = iTaskService.countUserTask(qIn);
        Long countTask = (Long) taskMap.get("total_task");
        if (countTask == null) {
            countTask = 0L;
        }
        out.put("countTask", countTask);

        /**
         * 完成任务数
         */
        qIn=new HashMap();
        qIn.put("userId", userInfo.getUserId());
        Map

        return out;
    }
}
