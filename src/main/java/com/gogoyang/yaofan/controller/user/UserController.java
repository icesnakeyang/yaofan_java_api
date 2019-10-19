package com.gogoyang.yaofan.controller.user;

import com.gogoyang.yaofan.business.user.IUserBusinessService;
import com.gogoyang.yaofan.controller.vo.Response;
import com.gogoyang.yaofan.utility.GogoActType;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final IUserBusinessService iUserBusinessService;
    private final ICommonBusinessService iCommonBusinessService;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public UserController(IUserBusinessService iUserBusinessService,
                          ICommonBusinessService iCommonBusinessService) {
        this.iUserBusinessService = iUserBusinessService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    @ResponseBody
    @PostMapping("/register")
    public Response register(@RequestBody UserRequest request) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            in.put("phone", request.getPhone());
            in.put("password", request.getPassword());
            iUserBusinessService.register(in);
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

    @ResponseBody
    @PostMapping("/login")
    public Response login(@RequestBody UserRequest request) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            in.put("phone", request.getPhone());
            in.put("password", request.getPassword());
            Map out = iUserBusinessService.login(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setCode(10001);
                Map logMap = new HashMap();
                logMap.put("GogoActType", GogoActType.LOGIN);
                logMap.put("memo", ex.getMessage());
                try {
                    iCommonBusinessService.createUserActLog(logMap);
                } catch (Exception ex3) {
                    logger.error(ex3.getMessage());
                }
                logger.error(ex.getMessage());
            }
        }
        try {
            Map logMap = new HashMap();
            logMap.put("GogoActType", GogoActType.LOGIN);
            logMap.put("memo", request.getPhone());
            iCommonBusinessService.createUserActLog(logMap);
        } catch (Exception ex4) {
            logger.error(ex4.getMessage());
        }
        return response;
    }
}
