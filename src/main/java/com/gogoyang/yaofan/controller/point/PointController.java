package com.gogoyang.yaofan.controller.point;

import com.gogoyang.yaofan.business.point.IPointBusinessService;
import com.gogoyang.yaofan.controller.vo.Response;
import com.gogoyang.yaofan.utility.GogoActType;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/point")
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
    @PostMapping("/applyPointWithdraw")
    public Response applyPointWithdraw(@RequestBody PointRequest request,
                                       HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("point", request.getPoint());
            in.put("remark", request.getRemark());
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.POINT_WITHDRAW);
            memoMap.put("point", request.getPoint());
            iPointBusinessService.applyPointWithdraw(in);
        } catch (Exception ex) {
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setCode(10001);
                logger.error(ex.getMessage());
            }
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }
}
