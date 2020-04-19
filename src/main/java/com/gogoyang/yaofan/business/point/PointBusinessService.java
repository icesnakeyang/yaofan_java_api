package com.gogoyang.yaofan.business.point;

import com.gogoyang.yaofan.business.statistic.IStatisticBusinessService;
import com.gogoyang.yaofan.meta.point.entity.PointLedger;
import com.gogoyang.yaofan.meta.point.service.IPointService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.meta.withdraw.service.IWithdrawService;
import com.gogoyang.yaofan.utility.GogoActType;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.GogoTools;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PointBusinessService implements IPointBusinessService {
    private final IPointService iPointService;
    private final ICommonBusinessService iCommonBusinessService;
    private final IStatisticBusinessService iStatisticBusinessService;
    private final IWithdrawService iWithdrawService;

    public PointBusinessService(IPointService iPointService,
                                ICommonBusinessService iCommonBusinessService,
                                IStatisticBusinessService iStatisticBusinessService,
                                IWithdrawService iWithdrawService) {
        this.iPointService = iPointService;
        this.iCommonBusinessService = iCommonBusinessService;
        this.iStatisticBusinessService = iStatisticBusinessService;
        this.iWithdrawService = iWithdrawService;
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

    /**
     * 统计用户积分
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map totalUserPoint(Map in) throws Exception {
        String token = in.get("token").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        Map map = iPointService.totalUserPoint(qIn);

        //计算用户的进账和出账积分
        Map out = new HashMap();
        if (map == null) {
            out.put("pointIn", 0);
            out.put("pointOut", 0);
            out.put("pointBalance", 0);
        } else {
            Double pIn = (Double) map.get("total_point_in");
            if (pIn == null) {
                pIn = 0.0;
            }
            Double pOut = (Double) map.get("total_point_out");
            if (pOut == null) {
                pOut = 0.0;
            }
            Double balance = pIn - pOut;
            out.put("pointIn", pIn);
            out.put("pointOut", pOut);
            out.put("pointBalance", balance);
        }

        //计算用户可以取现的积分
        qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        Double withdrawAble = iPointService.totalPointAccept(qIn);
        if(withdrawAble==null){
            withdrawAble=0.0;
        }
        Double unprocessPoint = iWithdrawService.sumWithdrawUnprocess(userInfo.getUserId());
        if(unprocessPoint==null){
            unprocessPoint=0.0;
        }
        withdrawAble -= unprocessPoint;
        out.put("withdrawAble", withdrawAble);

        return out;
    }
}
