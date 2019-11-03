package com.gogoyang.yaofan.meta.point.dao;

import com.gogoyang.yaofan.meta.point.entity.PointLedger;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PointDao {
    void createPointLedger(PointLedger pointLedger);
}
