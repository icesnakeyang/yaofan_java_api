package com.gogoyang.yaofan.meta.point.service;

import com.gogoyang.yaofan.meta.point.dao.PointActDao;
import com.gogoyang.yaofan.meta.point.dao.PointDao;
import com.gogoyang.yaofan.meta.point.entity.PointActLedger;
import com.gogoyang.yaofan.meta.point.entity.PointLedger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PointService implements IPointService {
    private final PointDao pointDao;
    private final PointActDao pointActDao;

    public PointService(PointDao pointDao, PointActDao pointActDao) {
        this.pointDao = pointDao;
        this.pointActDao = pointActDao;
    }

    @Override
    public void createPointLedger(PointLedger pointLedger) throws Exception {
        pointDao.createPointLedger(pointLedger);
    }

    @Override
    public Map totalUserPoint(Map qIn) throws Exception {
        Map out = pointDao.totalUserPoint(qIn);
        return out;
    }

    @Override
    public void createPointActLedger(PointActLedger pointActLedger) throws Exception {
        pointActDao.createPointActLedger(pointActLedger);
    }
}
