package com.gogoyang.yaofan.business.withdraw;

import com.gogoyang.yaofan.meta.point.entity.PointLedger;
import com.gogoyang.yaofan.meta.point.service.IPointService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.meta.withdraw.entity.Withdraw;
import com.gogoyang.yaofan.meta.withdraw.service.IWithdrawService;
import com.gogoyang.yaofan.utility.GogoActType;
import com.gogoyang.yaofan.utility.GogoTools;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import lombok.With;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class WithdrawBusinessService implements IWithdrawBusinessService {
    private final ICommonBusinessService iCommonBusinessService;
    private final IWithdrawService iWithdrawService;
    protected final IPointService iPointService;

    public WithdrawBusinessService(ICommonBusinessService iCommonBusinessService,
                                   IWithdrawService iWithdrawService,
                                   IPointService iPointService) {
        this.iCommonBusinessService = iCommonBusinessService;
        this.iWithdrawService = iWithdrawService;
        this.iPointService = iPointService;
    }

    /**
     * 用户申请积分取现
     *
     * @param in
     * @throws Exception
     */
    @Override
    public void withdrawPointApply(Map in) throws Exception {
        String token = in.get("token").toString();
        Double point = (Double) in.get("point");
        String remark = (String) in.get("remark");

        if (point == null) {
            throw new Exception("10021");
        }

        /**
         * 1、读取用户
         * 2、计算用户可以取现的积分
         * 3、创建取现申请
         */

        /**
         * 读取用户
         */
        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        /**
         * 检查用户积分余额
         */
        Map qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        Double withdrawAblePoint = iPointService.totalPointAccept(qIn);
        if (withdrawAblePoint == null) {
            withdrawAblePoint = 0.0;
        }
        //减去正在等待处理的取现申请
        Double unprocessPoint = iWithdrawService.sumWithdrawUnprocess(userInfo.getUserId());
        if (unprocessPoint == null) {
            unprocessPoint = 0.0;
        }
        withdrawAblePoint -= unprocessPoint;

        if (point > withdrawAblePoint) {
            //积分余额不足
            throw new Exception("10022");
        }

        /**
         * 创建积分兑换申请
         */
        Withdraw withdraw = new Withdraw();

        withdraw.setCreateTime(new Date());
        withdraw.setPoint(point);
        withdraw.setUserId(userInfo.getUserId());
        withdraw.setRemark(remark);
        withdraw.setWithdrawId(GogoTools.UUID().toString());

        iWithdrawService.createWithdraw(withdraw);
    }

    @Override
    public Map totalWithdraw(Map in) throws Exception {
        String token = in.get("token").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Integer totalWithdraw = iWithdrawService.totalWithdraw(userInfo.getUserId());

        Map out = new HashMap();
        out.put("totalWithdraw", totalWithdraw);

        return out;
    }

    /**
     * 读取用户未处理的取现申请列表
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listWithdrawUnProcess(Map in) throws Exception {
        String token = in.get("token").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        qIn.put("unProcess", true);
        ArrayList<Withdraw> withdraws = iWithdrawService.listWithdraw(qIn);

        Map out = new HashMap();
        out.put("withdraws", withdraws);

        return out;
    }
}
