package com.gogoyang.yaofan.meta.point.dao;

import com.gogoyang.yaofan.meta.point.entity.PointLedger;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface PointDao {
    void createPointLedger(PointLedger pointLedger);

    /**
     * 统计用户的总积分收入和总积分支出
     * @param qIn
     * userId
     * @return
     */
    Map totalUserPoint(Map qIn);

    List<Map> listUnProcessWithdraw(Map qIn);

    /**
     * 读取我的积分账户列表
     * @param qIn
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
     * total_point
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
