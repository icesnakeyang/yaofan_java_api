package com.gogoyang.yaofan.controller.stop;

import com.gogoyang.yaofan.business.stop.ITaskStopBusinessService;
import com.gogoyang.yaofan.controller.vo.Response;
import com.gogoyang.yaofan.utility.GogoActType;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/yaofanapi/taskStop")
public class TaskStopController {
    private final ITaskStopBusinessService iTaskStopBusinessService;
    private final ICommonBusinessService iCommonBusinessService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public TaskStopController(ITaskStopBusinessService iTaskStopBusinessService,
                              ICommonBusinessService iCommonBusinessService) {
        this.iTaskStopBusinessService = iTaskStopBusinessService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    /**
     * 甲方终止任务
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/stopTask")
    public Response stopTask(@RequestBody TaskStopRequest request,
                             HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("taskId", request.getTaskId());
            in.put("remark", request.getRemark());
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.STOP_TASK);
            memoMap.put("taskId", request.getTaskId());
            iTaskStopBusinessService.stopTask(in);
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
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }

    /**
     * 读取任务终止日志
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/getTaskStop")
    public Response getTaskStop(@RequestBody TaskStopRequest request,
                                HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("taskId", request.getTaskId());
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.GET_TASK_STOP);
            memoMap.put("taskId", request.getTaskId());
            Map out = iTaskStopBusinessService.getTaskStop(in);
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
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }
}
