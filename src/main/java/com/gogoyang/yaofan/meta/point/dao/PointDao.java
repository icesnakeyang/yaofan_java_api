package com.gogoyang.yaofan.meta.point.dao;

import com.gogoyang.yaofan.meta.point.entity.PointLedger;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PointDao {
    void createPointLedger(PointLedger pointLedger);

    Map totalUserPoint(Map qIn);

    List<Map> listUnProcessWithdraw(Map qIn);
}
