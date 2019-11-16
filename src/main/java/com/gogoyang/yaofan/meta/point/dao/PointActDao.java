package com.gogoyang.yaofan.meta.point.dao;

import com.gogoyang.yaofan.meta.point.entity.PointActLedger;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PointActDao {
    void createPointActLedger(PointActLedger pointActLedger);
}
