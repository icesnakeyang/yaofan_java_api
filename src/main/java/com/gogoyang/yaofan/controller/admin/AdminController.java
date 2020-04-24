package com.gogoyang.yaofan.controller.admin;

import com.gogoyang.yaofan.business.admin.IAdminBusinessService;
import com.gogoyang.yaofan.controller.vo.Response;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/yaofanapi/admin")
public class AdminController {
    private final IAdminBusinessService iAdminBusinessService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public AdminController(IAdminBusinessService iAdminBusinessService) {
        this.iAdminBusinessService = iAdminBusinessService;
    }

    @ResponseBody
    @PostMapping("/createAdmin")
    public Response createAdmin(@RequestBody AdminRequest request,
                                HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("loginName", request.getLoginName());
            in.put("password", request.getPassword());
            iAdminBusinessService.createAdmin(in);
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
    @PostMapping("/loginAdmin")
    public Response loginAdmin(@RequestBody AdminRequest request) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            in.put("loginName", request.getLoginName());
            in.put("password", request.getPassword());
            Map out=iAdminBusinessService.loginAdmin(in);
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

    @ResponseBody
    @PostMapping("/listRegisterUser")
    public Response listRegisterUser(@RequestBody AdminRequest request,
                                     HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token=httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Map out=iAdminBusinessService.listRegisterUser(in);
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
     * 查询一个用户的详情
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/getUser")
    public Response getUser(@RequestBody AdminRequest request,
                                     HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token=httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("userId", request.getUserId());
            Map out=iAdminBusinessService.getUser(in);
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
     * 查询用户日志列表
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listUserAction")
    public Response listUserAction(@RequestBody AdminRequest request,
                                     HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token=httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("userId", request.getUserId());
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            in.put("startTime", request.getStartTime());
            in.put("endTime", request.getEndTime());
            Map out=iAdminBusinessService.listUserAction(in);
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
     * 统计用户行为数据
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/totalUserActionType")
    public Response totalUserActionType(@RequestBody AdminRequest request,
                                     HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token=httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("userId", request.getUserId());
            Map out=iAdminBusinessService.totalUserActionType(in);
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
}
