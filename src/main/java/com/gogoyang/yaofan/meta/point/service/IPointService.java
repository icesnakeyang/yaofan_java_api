package com.gogoyang.yaofan.meta.point.service;

import com.gogoyang.yaofan.meta.point.entity.PointLedger;

public interface IPointService {
    void createPointLedger(PointLedger pointLedger) throws Exception;
}
