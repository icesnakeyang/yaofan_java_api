package com.gogoyang.yaofan.controller.statistic;

import com.gogoyang.yaofan.business.statistic.IStatisticBusinessService;
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
@RequestMapping("/statistic")
public class StatisticController {
    private final IStatisticBusinessService iStatisticBusinessService;
    private final ICommonBusinessService iCommonBusinessService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public StatisticController(IStatisticBusinessService iStatisticBusinessService,
                               ICommonBusinessService iCommonBusinessService) {
        this.iStatisticBusinessService = iStatisticBusinessService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    @ResponseBody
    @PostMapping("dashboard")
    public Response dashboard(@RequestBody MassRequest request,
                              HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.DASHBOARD);

            Map out = iStatisticBusinessService.dashboard(in);
            response.setData(out);
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
