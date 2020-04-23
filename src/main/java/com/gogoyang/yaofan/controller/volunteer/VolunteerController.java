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
     * 读取我的义工任务列表
     * 包括我创建的，和我承接的
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listMyVolunteerTask")
    public Response listMyVolunteerTask(@RequestBody VolunteerRequest request,
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
            Map out=iVolunteerBusinessService.listMyVolunteerTask(in);
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

    /**
     * 报名申请义工任务
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/applyVolunteerTask")
    public Response applyVolunteerTask(@RequestBody VolunteerRequest request,
                                       HttpServletRequest httpServletRequest){
        Response response=new Response();
        Map in=new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            String token=httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("remark", request.getRemark());
            in.put("volunteerTaskId", request.getVolunteerTaskId());
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.APPLY_VOLUNTEER_TASK);
            memoMap.put("volunteerTaskId", request.getVolunteerTaskId());
            iVolunteerBusinessService.applyVolunteerTask(in);
            memoMap.put("result", GogoStatus.SUCCESS);
        }catch (Exception ex){
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setCode(10001);
                logger.error(ex2.getMessage());
            }
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
     * 查询申请我的义工任务人员列表
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listMyVolunteerTaskApply")
    public Response listMyVolunteerTaskApply(@RequestBody VolunteerRequest request,
                                       HttpServletRequest httpServletRequest){
        Response response=new Response();
        Map in=new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            String token=httpServletRequest.getHeader("token");
            in.put("token", token);
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.LIST_VOLUNTEER_APPLY);
            Map out=iVolunteerBusinessService.listMyVolunteerTaskApply(in);
            response.setData(out);
            memoMap.put("result", GogoStatus.SUCCESS);
        }catch (Exception ex){
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setCode(10001);
                logger.error(ex2.getMessage());
            }
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
     * 查询我申请的义工列表
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listMyVolunteerTaskApplyJoin")
    public Response listMyVolunteerTaskApplyJoin(@RequestBody VolunteerRequest request,
                                             HttpServletRequest httpServletRequest){
        Response response=new Response();
        Map in=new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            String token=httpServletRequest.getHeader("token");
            in.put("token", token);
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.LIST_VOLUNTEER_APPLY);
            Map out=iVolunteerBusinessService.listMyVolunteerTaskApplyJoin(in);
            response.setData(out);
            memoMap.put("result", GogoStatus.SUCCESS);
        }catch (Exception ex){
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setCode(10001);
                logger.error(ex2.getMessage());
            }
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
     * 查询义工任务申请详情
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/getVolunteerApply")
    public Response getVolunteerApply(@RequestBody VolunteerRequest request,
                                     HttpServletRequest httpServletRequest){
        Response response=new Response();
        Map in=new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            String token=httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("volunteerApplyId", request.getVolunteerApplyId());
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.GET_VOLUNTEER_APPLY);
            Map out=iVolunteerBusinessService.getVolunteerApply(in);
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
     * 拒绝义工任务申请
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/rejectVolunteerApply")
    public Response rejectVolunteerApply(@RequestBody VolunteerRequest request,
                                     HttpServletRequest httpServletRequest){
        Response response=new Response();
        Map in=new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            String token=httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("volunteerApplyId", request.getVolunteerApplyId());
            in.put("remark", request.getRemark());
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.REJECT_VOLUNTEER_APPLY);
            iVolunteerBusinessService.rejectVolunteerApply(in);
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
     * 同意义工任务申请
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/agreeVolunteerApply")
    public Response agreeVolunteerApply(@RequestBody VolunteerRequest request,
                                     HttpServletRequest httpServletRequest){
        Response response=new Response();
        Map in=new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            String token=httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("volunteerApplyId", request.getVolunteerApplyId());
            in.put("remark", request.getRemark());
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.AGREE_VOLUNTEER_APPLY);
            iVolunteerBusinessService.agreeVolunteerApply(in);
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
     * 查询我的已经通过审核的义工
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listMyVolunteerAgree")
    public Response listMyVolunteerAgree(@RequestBody VolunteerRequest request,
                                     HttpServletRequest httpServletRequest){
        Response response=new Response();
        Map in=new HashMap();
        try {
            String token=httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Map out=iVolunteerBusinessService.listMyVolunteerAgree(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setCode(10001);
                logger.error(ex.getMessage());
            }
        }
        return  response;
    }

    /**
     * 统计我的义工任务总数，以及未读状态
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/totalMyVolunteer")
    public Response totalMyVolunteer(@RequestBody VolunteerRequest request,
                                     HttpServletRequest httpServletRequest){
        Response response=new Response();
        Map in=new HashMap();
        try {
            String token=httpServletRequest.getHeader("token");
            in.put("token", token);
            Map out=iVolunteerBusinessService.totalMyVolunteer(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setCode(10001);
                logger.error(ex.getMessage());
            }
        }
        return  response;
    }

    /**
     * 终止义工任务招募
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/stopVolunteerTask")
    public Response stopVolunteerTask(@RequestBody VolunteerRequest request,
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
            logMap.put("GogoActType", GogoActType.STOP_VOLUNTEER_TASK);
            iVolunteerBusinessService.stopVolunteerTask(in);
            memoMap.put("result", GogoStatus.SUCCESS);
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
