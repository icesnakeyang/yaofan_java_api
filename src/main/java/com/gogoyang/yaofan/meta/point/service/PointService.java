package com.gogoyang.yaofan.meta.point.service;

import com.gogoyang.yaofan.meta.point.dao.PointDao;
import com.gogoyang.yaofan.meta.point.entity.PointLedger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PointService implements IPointService {
    private final PointDao pointDao;

    public PointService(PointDao pointDao) {
        this.pointDao = pointDao;
    }

    @Override
    public void createPointLedger(PointLedger pointLedger) throws Exception {
        pointDao.createPointLedger(pointLedger);
    }

    /**
     * 统计用户的总积分收入和总积分支出
     * @param qIn
     * userId
     * @return
     * @throws Exception
     */
    @Override
    public Map totalUserPoint(Map qIn) throws Exception {
        Map out = pointDao.totalUserPoint(qIn);
        return out;
    }

    @Override
    public List<Map> listUnProcessWithdraw(Map qIn) throws Exception {
        List<Map> out = pointDao.listUnProcessWithdraw(qIn);
        return out;
    }

    /**
     * 读取我的积分账户列表
     * @param qIn
     * userId
     * offset
     * size
     * @return
     */
    @Override
    public ArrayList<PointLedger> listMyPointLedger(Map qIn) {
        ArrayList<PointLedger> pointLedgers=pointDao.listMyPointLedger(qIn);
        return pointLedgers;
    }

    /**
     * 统计用户可以取现的积分
     * @param qIn
     * userId
     * @return
     */
    @Override
    public Double totalPointAccept(Map qIn) {
        Double point=pointDao.totalPointAccept(qIn);
        return point;
    }

    /**
     * 统计我的积分账户列表总数
     * @param qIn
     * userId
     * startDate
     * endDate
     * @return
     */
    @Override
    public Integer totalMyPointLedger(Map qIn) {
        Integer total=pointDao.totalMyPointLedger(qIn);
        return total;
    }
}
