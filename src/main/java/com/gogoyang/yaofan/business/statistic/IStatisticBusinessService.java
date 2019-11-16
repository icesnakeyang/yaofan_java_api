package com.gogoyang.yaofan.business.statistic;

import java.util.Map;

public interface IStatisticBusinessService {
    Map dashboard(Map in) throws Exception;
    Double calPointBalance(String userId) throws Exception;
}
