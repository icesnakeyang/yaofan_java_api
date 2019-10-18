package com.gogoyang.yaofan.controller.user;

import com.gogoyang.yaofan.business.user.IUserBusinessService;
import com.gogoyang.yaofan.controller.vo.Response;
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
    private Logger logger = LoggerFactory.getLogger(getClass());

    public UserController(IUserBusinessService iUserBusinessService) {
        this.iUserBusinessService = iUserBusinessService;
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
}
