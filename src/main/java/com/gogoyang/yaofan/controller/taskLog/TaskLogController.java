package com.gogoyang.yaofan.controller.taskLog;

import com.gogoyang.yaofan.business.taskLog.ITaskLogBusinessService;
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
@RequestMapping("/tasklog")
public class TaskLogController {
    private final ITaskLogBusinessService iTaskLogBusinessService;
    private final ICommonBusinessService iCommonBusinessService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public TaskLogController(ITaskLogBusinessService iTaskLogBusinessService,
                             ICommonBusinessService iCommonBusinessService) {
        this.iTaskLogBusinessService = iTaskLogBusinessService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    @ResponseBody
    @PostMapping("/createTaskLog")
    public Response createTaskLog(@RequestBody TaskLogRequest request,
                                  HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            logMap.put("token", token);
            logMap.put("taskId", request.getTaskId());
            memoMap.put("taskId", request.getTaskId());
            logMap.put("GogoActType", GogoActType.CREATE_TASK_LOG);
            iTaskLogBusinessService.createTaskLog(in);
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
            logMap.put("memeo", memoMap);
            iCommonBusinessService.createUserActLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }
}
