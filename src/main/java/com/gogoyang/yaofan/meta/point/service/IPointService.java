package com.gogoyang.yaofan.meta.point.service;

import com.gogoyang.yaofan.meta.point.entity.PointLedger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IPointService {
    void createPointLedger(PointLedger pointLedger) throws Exception;

    /**
     * 统计用户的总积分收入和总积分支出
     * @param qIn
     * userId
     * @return
     * @throws Exception
     */
    Map totalUserPoint(Map qIn) throws Exception;

    List<Map> listUnProcessWithdraw(Map qIn) throws Exception;

    /**
     * 读取我的积分账户列表
     * @param qIn
     * userId
     * userId
     * startDate
     * endDate
     * offset
     * size
     * @return
     */
    ArrayList<PointLedger> listMyPointLedger(Map qIn);

    /**
     * 统计用户可以取现的积分
     * @param qIn
     * userId
     * @return
     */
    Double totalPointAccept(Map qIn);

    /**
     * 统计我的积分账户列表总数
     * @param qIn
     * userId
     * startDate
     * endDate
     * @return
     */
    Integer totalMyPointLedger(Map qIn);

}
