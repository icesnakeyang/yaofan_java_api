package com.gogoyang.yaofan.business.statistic;

import com.gogoyang.yaofan.meta.point.service.IPointService;
import com.gogoyang.yaofan.meta.task.service.ITaskService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.utility.GogoTools;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        taskMap = iTaskService.countUserTaskComplete(qIn);
        Long countTaskComplete = (Long) taskMap.get("total_complete");
        if (countTaskComplete == null) {
            countTaskComplete = 0L;
        }
        out.put("countTaskComplete", countTaskComplete);

        /**
         * 统计用户进行中的任务总数
         */
        qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        taskMap = iTaskService.countUserTaskProgress(qIn);
        Long countTaskProgress = (Long) taskMap.get("total_progress");
        if (countTaskProgress == null) {
            countTaskProgress = 0L;
        }
        out.put("countTaskProgress", countTaskProgress);

        /**
         * 统计本月积分收入和支出
         */
        qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());

        //获取本月1号0点的起始时间
        Date dateStart = GogoTools.dateStartOfThisMonth();
        //获取下月1号0点的终止时间
        Date dateEnd = GogoTools.dateEndOfThisMonth();
        qIn.put("dateStart", dateStart);
        qIn.put("dateEnd", dateEnd);
        taskMap = iTaskService.totalPointIn(qIn);
        //本月收入
        Double totalIn = (Double) taskMap.get("total_in");
        if (totalIn == null) {
            totalIn = 0.0;
        }
        out.put("totalInMonth", totalIn);
        //本月支出
        Double totalOut = (Double) taskMap.get("total_out");
        if (totalOut == null) {
            totalOut = 0.0;
        }
        out.put("totalOutMonth", totalOut);


        return out;
    }
}
