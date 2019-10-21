package com.gogoyang.yaofan.controller.user;

import com.gogoyang.yaofan.business.user.IUserBusinessService;
import com.gogoyang.yaofan.controller.vo.Response;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
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
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        logMap.put("GogoActType", GogoActType.REGISTER);
        try {
            in.put("phone", request.getPhone());
            memoMap.put("phone", request.getPhone());
            in.put("password", request.getPassword());
            Map out = iUserBusinessService.register(in);
            response.setData(out);
            UserInfo userInfo = (UserInfo) out.get("userInfo");
            if (userInfo != null) {
                logMap.put("userId", userInfo.getUserId());
            }
        } catch (Exception ex) {
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
                memoMap.put("error", ex.getMessage());
            } catch (Exception ex2) {
                response.setCode(10001);
                memoMap.put("error", ex.getMessage());
                logger.error(ex.getMessage());
            }
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/login")
    public Response login(@RequestBody UserRequest request) {
        Response response = new Response();
        Map in = new HashMap();
        Map memoMap = new HashMap();
        Map logMap = new HashMap();
        logMap.put("GogoActType", GogoActType.LOGIN);
        try {
            in.put("phone", request.getPhone());
            memoMap.put("phone", request.getPhone());
            in.put("password", request.getPassword());
            Map out = iUserBusinessService.login(in);
            UserInfo userInfo = (UserInfo) out.get("userInfo");
            if (userInfo != null) {
                logMap.put("userId", userInfo.getUserId());
            }
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
                memoMap.put("error", ex.getMessage());
            } catch (Exception ex2) {
                response.setCode(10001);
                memoMap.put("error", ex.getMessage());
                logger.error(ex.getMessage());
            }
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/loginByToken")
    public Response loginByToken(@RequestBody UserRequest request,
                                 HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        logMap.put("GogoActType", GogoActType.LOGIN);
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            memoMap.put("token", token);
            Map out = iUserBusinessService.loginByToken(in);
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
        logMap.put("memo", memoMap);
        try {
            iCommonBusinessService.createUserActLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }
}
