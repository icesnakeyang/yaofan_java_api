package com.gogoyang.yaofan.controller.point;

import com.gogoyang.yaofan.business.point.IPointBusinessService;
import com.gogoyang.yaofan.controller.vo.Response;
import com.gogoyang.yaofan.utility.GogoActType;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/yaofanapi/point")
public class PointController {
    private final ICommonBusinessService iCommonBusinessService;
    private final IPointBusinessService iPointBusinessService;

    private Logger logger = LoggerFactory.getLogger(getClass());


    public PointController(ICommonBusinessService iCommonBusinessService,
                           IPointBusinessService iPointBusinessService) {
        this.iCommonBusinessService = iCommonBusinessService;
        this.iPointBusinessService = iPointBusinessService;
    }

    @ResponseBody
    @PostMapping("listUnProcessWithdraw")
    public Response listUnProcessWithdraw(@RequestBody PointRequest request,
                                          HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            Map out = iPointBusinessService.listPointWithdrawApply(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setCode(10001);
                logger.error(ex.getMessage());
            }
        }
        return response;
    }

    /**
     * 读取我的积分账户列表
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("listMyPointLedger")
    public Response listMyPointLedger(@RequestBody PointRequest request,
                                          HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.LIST_MY_POINT);
            Map out = iPointBusinessService.listMyPointLedger(in);
            response.setData(out);
            logMap.put("result", GogoStatus.SUCCESS);
        } catch (Exception ex) {
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setCode(10001);
                logger.error(ex.getMessage());
            }
            logMap.put("result", GogoStatus.FAILED);
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActLog(logMap);
        }catch (Exception ex3){
            logger.error(ex3.getMessage());
        }
        return response;
    }

    /**
     * 统计我的积分账户
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("totalUserPoint")
    public Response totalUserPoint(@RequestBody PointRequest request,
                                          HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.TOTAL_USER_POINT);
            Map out = iPointBusinessService.totalUserPoint(in);
            response.setData(out);
            logMap.put("result", GogoStatus.SUCCESS);
        } catch (Exception ex) {
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setCode(10001);
                logger.error(ex.getMessage());
            }
            logMap.put("result", GogoStatus.FAILED);
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActLog(logMap);
        }catch (Exception ex3){
            logger.error(ex3.getMessage());
        }
        return response;
    }
}
