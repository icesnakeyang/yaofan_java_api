package com.gogoyang.yaofan.business.statistic;

import com.gogoyang.yaofan.meta.point.service.IPointService;
import com.gogoyang.yaofan.meta.task.service.ITaskService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.utility.GogoStatus;
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

        Map out = calPoint(userInfo.getUserId());

        /**
         * 用户的任务总数
         */
        Map qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        Integer totalTasks = iTaskService.totalUserTask(qIn);
        out.put("totalTasks", totalTasks);

        /**
         * 完成任务数
         */
        qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        qIn.put("status", GogoStatus.COMPLETE);
        Integer totalTasksComplete = iTaskService.totalUserTask(qIn);
        out.put("totalTasksComplete", totalTasksComplete);

        /**
         * 统计用户进行中的任务总数
         */
        qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        qIn.put("status", GogoStatus.PROGRESS);
        Integer totalTasksProgress = iTaskService.totalUserTask(qIn);

        out.put("totalTasksProgress", totalTasksProgress);

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
        Map taskMap = iTaskService.totalPointIn(qIn);
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

    /**
     * 查询返回用户当前的积分余额
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public Double calPointBalance(String userId) throws Exception {
        Map out = calPoint(userId);
        Double point = (Double) out.get("currentPoint");
        if (point == null) {
            point = 0.0;
        }
        return point;
    }

    /**
     * 查询计算用户当前的积分余额，积分收入，积分支出
     *
     * @param userId
     * @return currentPoint
     * pointIn
     * pointIn
     * @throws Exception
     */
    private Map calPoint(String userId) throws Exception {
        UserInfo userInfo = iCommonBusinessService.getUserByUserId(userId);

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

        return out;
    }
}
