package com.gogoyang.yaofan.business.point;

import java.util.Map;

public interface IPointBusinessService {
    void applyPointWithdraw(Map in) throws Exception;

    Map listPointWithdrawApply(Map in) throws Exception;

    void approvePointWithdraw(Map in) throws Exception;

    Map listMyPointLedger(Map in) throws Exception;

    Map totalUserPoint(Map in) throws Exception;
}
