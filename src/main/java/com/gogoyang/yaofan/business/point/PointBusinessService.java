package com.gogoyang.yaofan.business.point;

import com.gogoyang.yaofan.business.statistic.IStatisticBusinessService;
import com.gogoyang.yaofan.meta.point.entity.PointLedger;
import com.gogoyang.yaofan.meta.point.service.IPointService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.utility.GogoActType;
import com.gogoyang.yaofan.utility.GogoTools;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PointBusinessService implements IPointBusinessService {
    private final IPointService iPointService;
    private final ICommonBusinessService iCommonBusinessService;
    private final IStatisticBusinessService iStatisticBusinessService;

    public PointBusinessService(IPointService iPointService,
                                ICommonBusinessService iCommonBusinessService,
                                IStatisticBusinessService iStatisticBusinessService) {
        this.iPointService = iPointService;
        this.iCommonBusinessService = iCommonBusinessService;
        this.iStatisticBusinessService = iStatisticBusinessService;
    }

    /**
     * 用户申请一次兑换积分
     * 把积分兑换掉
     * 创建一个积分兑换记录，后台管理员处理兑换申请。
     *
     * @param in
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void applyPointWithdraw(Map in) throws Exception {
        String token = in.get("token").toString();
        Double point = (Double) in.get("point");
        String remark = (String) in.get("remark");

        if (point == null) {
            throw new Exception("10021");
        }

        /**
         * 读取用户
         */
        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        /**
         * 检查用户积分余额
         */

        Double pointBalance = iStatisticBusinessService.calPointBalance(userInfo.getUserId());

        if (point > pointBalance) {
            //积分余额不足
            throw new Exception("10022");
        }

        /**
         * 创建积分兑换申请
         * 先扣除用户积分，也可以说是暂时冻结
         */
        PointLedger pointLedger = new PointLedger();

        pointLedger.setActType(GogoActType.POINT_WITHDRAW.toString());
        pointLedger.setCreateTime(new Date());
        pointLedger.setPointOut(point);
        pointLedger.setUserId(userInfo.getUserId());
        pointLedger.setRemark(remark);
        pointLedger.setPointLedgerId(GogoTools.UUID().toString());

        iPointService.createPointLedger(pointLedger);
    }

    @Override
    public Map listPointWithdrawApply(Map in) throws Exception {
        String token = in.get("token").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        /**
         * 用户必须是管理员，且具备批准积分兑换的权限
         */

        Map qIn = new HashMap();

        List<Map> pointMap = iPointService.listUnProcessWithdraw(qIn);

        Map out = new HashMap();
        out.put("pointWithdrawApplyList", pointMap);
        return out;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void approvePointWithdraw(Map in) throws Exception {
        String token = in.get("token").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        /**
         * 用户必须是管理员，且具备批准积分兑换的权限
         */
    }

    @Override
    public Map listMyPointLedger(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        Integer offset = (pageIndex - 1) * pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<PointLedger> pointLedgers = iPointService.listMyPointLedger(qIn);

        Map out = new HashMap();
        out.put("pointLedgers", pointLedgers);

        return out;
    }

    @Override
    public Map totalUserPoint(Map in) throws Exception {
        String token = in.get("token").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        Map map = iPointService.totalUserPoint(qIn);

        return map;
    }
}
