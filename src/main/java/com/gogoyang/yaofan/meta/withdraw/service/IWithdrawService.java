package com.gogoyang.yaofan.meta.withdraw.service;

import com.gogoyang.yaofan.meta.withdraw.entity.Withdraw;

import java.util.ArrayList;
import java.util.Map;

public interface IWithdrawService {
    /**
     * 创建一个取现日志
     * @param withdraw
     */
    void createWithdraw(Withdraw withdraw);

    /**
     * 汇总用户已申请还未处理的取现总积分
     * @param userId
     * @return
     */
    Double sumWithdrawUnprocess(String userId);

    /**
     * 统计用户的取现申请总数
     * @param userId
     * @return
     */
    Integer totalWithdraw(String userId);

    /**
     * 读取用户的取现申请列表
     * @param qIn
     * userId
     * unRead=true(未读)
     * unProcess=true(未处理)
     * unReadProcess=true(处理结果未读)
     * processResult：处理结果状态
     * @return
     */
    ArrayList<Withdraw> listWithdraw(Map qIn);
}
