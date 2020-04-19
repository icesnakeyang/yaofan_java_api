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
}
