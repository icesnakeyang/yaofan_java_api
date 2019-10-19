package com.gogoyang.yaofan.controller.team;

import com.gogoyang.yaofan.business.team.ITeamBusinessService;
import com.gogoyang.yaofan.controller.vo.Response;
import com.gogoyang.yaofan.meta.team.entity.Team;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/team")
public class TeamController {
    protected final ITeamBusinessService iTeamBusinessService;
    private final ICommonBusinessService iCommonBusinessService;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public TeamController(ITeamBusinessService iTeamBusinessService,
                          ICommonBusinessService iCommonBusinessService) {
        this.iTeamBusinessService = iTeamBusinessService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    @ResponseBody
    @PostMapping("/createTeam")
    public Response createTeam(@RequestBody TeamRequest request,
                               HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map out = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("name", request.getName());
            in.put("description", request.getDescription());
            out = iTeamBusinessService.createTeam(in);
            Team team = (Team) out.get("team");
            response.setData(team);
            memoMap.put("name", team.getName());
            logMap.put("userId", team.getUserId());
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
}
