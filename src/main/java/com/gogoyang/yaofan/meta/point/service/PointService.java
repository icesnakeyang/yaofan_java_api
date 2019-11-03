package com.gogoyang.yaofan.meta.point.service;

import com.gogoyang.yaofan.meta.point.dao.PointDao;
import com.gogoyang.yaofan.meta.point.entity.PointLedger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
