package com.gogoyang.yaofan.meta.point.service;

import com.gogoyang.yaofan.meta.point.entity.PointActLedger;
import com.gogoyang.yaofan.meta.point.entity.PointLedger;

import java.util.List;
import java.util.Map;

public interface IPointService {
    void createPointLedger(PointLedger pointLedger) throws Exception;

    Map totalUserPoint(Map qIn) throws Exception;

    void createPointActLedger(PointActLedger pointActLedger) throws Exception;
}
