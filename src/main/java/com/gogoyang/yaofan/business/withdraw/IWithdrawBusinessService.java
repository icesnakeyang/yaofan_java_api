package com.gogoyang.yaofan.business.withdraw;

import java.util.Map;

public interface IWithdrawBusinessService {
    void withdrawPointApply(Map in) throws Exception;

    Map totalWithdraw(Map in) throws Exception;

    /**
     * 读取用户未处理的取现申请列表
     * @param in
     * @return
     * @throws Exception
     */
    Map listWithdrawUnProcess(Map in) throws Exception;
}
