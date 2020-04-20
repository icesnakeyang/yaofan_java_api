package com.gogoyang.yaofan.controller.volunteer;

import com.gogoyang.yaofan.business.volunteer.IVolunteerBusinessService;
import com.gogoyang.yaofan.controller.vo.Response;
import com.gogoyang.yaofan.utility.GogoActType;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.GogoTools;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/yaofanapi/volunteer")
public class VolunteerController {
    private final ICommonBusinessService iCommonBusinessService;
    private final IVolunteerBusinessService iVolunteerBusinessService;

    private Logger logger= LoggerFactory.getLogger(getClass());

    public VolunteerController(ICommonBusinessService iCommonBusinessService,
                         IVolunteerBusinessService iVolunteerBusinessService) {
        this.iCommonBusinessService = iCommonBusinessService;
        this.iVolunteerBusinessService = iVolunteerBusinessService;
    }

    /**
     * 创建义工任务
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/createVolunteerTask")
    public Response createVolunteerTask(@RequestBody VolunteerRequest request,
                                        HttpServletRequest httpServletRequest){
        Response response=new Response();
        Map in=new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            String token=httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("title", request.getTitle());
            in.put("content", request.getContent());
            String theDate=request.getTheDate();
            String theTime=request.getTheTime();
            Date startTime=GogoTools.strToDatetime2(theDate+" "+theTime);
            in.put("startTime", startTime);
            in.put("pid", request.getPid());
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.CREATE_VOLUNTEER_TASK);
            iVolunteerBusinessService.createVolunteerTask(in);
            logMap.put("result", GogoStatus.SUCCESS);
        }catch (Exception ex){
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setCode(10001);
                logger.error(ex.getMessage());
            }
            memoMap.put("result", GogoStatus.FAILED);
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActLog(logMap);
        }catch (Exception ex3){
            logger.error(ex3.getMessage());
        }
        return  response;
    }

    /**
     * 读取义工任务列表
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listVolunteerTask")
    public Response listVolunteerTask(@RequestBody VolunteerRequest request,
                                        HttpServletRequest httpServletRequest){
        Response response=new Response();
        Map in=new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            String token=httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.LIST_VOLUNTEER_TASK);
            Map out=iVolunteerBusinessService.listVolunteerTask(in);
            response.setData(out);
            logMap.put("result", GogoStatus.SUCCESS);
        }catch (Exception ex){
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setCode(10001);
                logger.error(ex.getMessage());
            }
            memoMap.put("result", GogoStatus.FAILED);
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActLog(logMap);
        }catch (Exception ex3){
            logger.error(ex3.getMessage());
        }
        return  response;
    }

    /**
     * 根据volunteerTaskId查询义工任务详情
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/getVolunteerTask")
    public Response getVolunteerTask(@RequestBody VolunteerRequest request,
                                      HttpServletRequest httpServletRequest){
        Response response=new Response();
        Map in=new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            String token=httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("volunteerTaskId", request.getVolunteerTaskId());
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.GET_VOLUNTEER_TASK);
            Map out=iVolunteerBusinessService.getVolunteerTask(in);
            response.setData(out);
            logMap.put("result", GogoStatus.SUCCESS);
        }catch (Exception ex){
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setCode(10001);
                logger.error(ex.getMessage());
            }
            memoMap.put("result", GogoStatus.FAILED);
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActLog(logMap);
        }catch (Exception ex3){
            logger.error(ex3.getMessage());
        }
        return  response;
    }
}
