package com.gogoyang.yaofan.meta.withdraw.service;

import com.gogoyang.yaofan.meta.withdraw.dao.WithdrawDao;
import com.gogoyang.yaofan.meta.withdraw.entity.Withdraw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class WithdrawService implements IWithdrawService {
    private final WithdrawDao withdrawDao;

    public WithdrawService(WithdrawDao withdrawDao) {
        this.withdrawDao = withdrawDao;
    }

    /**
     * 创建一个取现日志
     *
     * @param withdraw
     */
    @Override
    public void createWithdraw(Withdraw withdraw) {
        withdrawDao.createWithdraw(withdraw);
    }

    /**
     * 汇总用户已申请还未处理的取现总积分
     *
     * @param userId
     * @return
     */
    @Override
    public Double sumWithdrawUnprocess(String userId) {
        Double total = withdrawDao.sumWithdrawUnprocess(userId);
        return total;
    }

    /**
     * 统计用户的取现申请总数
     *
     * @param userId
     * @return
     */
    @Override
    public Integer totalWithdraw(String userId) {
        Integer total = withdrawDao.totalWithdraw(userId);
        return total;
    }

    /**
     * 读取用户的取现申请列表
     *
     * @param qIn userId
     *            unRead=true(未读)
     *            unProcess=true(未处理)
     *            unReadProcess=true(处理结果未读)
     *            processResult：处理结果状态
     * @return
     */
    @Override
    public ArrayList<Withdraw> listWithdraw(Map qIn) {
        ArrayList<Withdraw> withdraws = withdrawDao.listWithdraw(qIn);
        return withdraws;
    }
}
