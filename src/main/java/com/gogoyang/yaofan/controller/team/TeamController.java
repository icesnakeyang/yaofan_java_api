package com.gogoyang.yaofan.controller.team;

import com.gogoyang.yaofan.business.team.ITeamBusinessService;
import com.gogoyang.yaofan.controller.vo.Response;
import com.gogoyang.yaofan.meta.team.entity.Team;
import com.gogoyang.yaofan.utility.GogoActType;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/team")
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
            in.put("token", token);
            logMap.put("token", token);
            in.put("name", request.getName());
            memoMap.put("name", request.getName());
            in.put("description", request.getDescription());
            out = iTeamBusinessService.createTeam(in);
            Team team = (Team) out.get("team");
            response.setData(team);
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
            logMap.put("GogoActType", GogoActType.CREATE_TEAM);
            iCommonBusinessService.createUserActLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }

    @ResponseBody
    @PostMapping("listMyTeam")
    public Response listMyTeam(@RequestBody TeamRequest request,
                               HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.LIST_TEAM);
            Map out = iTeamBusinessService.listMyTeam(in);
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

    @ResponseBody
    @PostMapping("/searchTeam")
    public Response searchTeam(@RequestBody TeamRequest request,
                               HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            logMap.put("GogoActType", GogoActType.SEARCH_TEAM);
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            logMap.put("token", token);
            in.put("name", request.getName());
            memoMap.put("search_key", request.getName());
            Map out = iTeamBusinessService.searchTeam(in);
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
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/getTeamByTeamId")
    public Response getTeamByTeamId(@RequestBody TeamRequest request,
                                    HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.GET_TEAM);
            in.put("teamId", request.getTeamId());
            memoMap.put("teamId", request.getTeamId());
            Map out = iTeamBusinessService.getTeamByTeamId(in);
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
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }

    @ResponseBody
    @PostMapping("applyTeam")
    public Response applyTeam(@RequestBody TeamRequest request,
                              HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        Map in = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            logMap.put("token", token);
            in.put("remark", request.getRemark());
            in.put("teamId", request.getTeamId());
            logMap.put("GogoActType", GogoActType.APPLY_TEAM);
            iTeamBusinessService.applyTeam(in);
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
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }

    /**
     * 读取我申请的团队日志
     * 包括已处理和未处理的
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listTeamApplyLogMyApply")
    public Response listTeamApplyLogMyApply(@RequestBody TeamRequest request,
                                  HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.LIST_APPLY_TEAM);
            Map out = iTeamBusinessService.listTeamApplyLogMyApply(in);
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
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }

    /**
     * 读取申请我的团队日志
     * 包括已处理和未处理的
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listTeamApplyLogApplyUser")
    public Response listTeamApplyLogApplyUser(@RequestBody TeamRequest request,
                                  HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.LIST_APPLY_TEAM);
            Map out = iTeamBusinessService.listTeamApplyLogApplyUser(in);
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
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }

    @ResponseBody
    @PostMapping("getTeamApplyLog")
    public Response getTeamApplyLog(@RequestBody TeamRequest request,
                                 HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map logMap = new HashMap();
        Map in = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.GET_APPLY_TEAM);
            in.put("teamApplyLogId", request.getTeamApplyLogId());
            memoMap.put("teamApplyLogId", request.getTeamApplyLogId());
            Map out = iTeamBusinessService.getTeamApplyLog(in);
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
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }

    @ResponseBody
    @PostMapping("rejectApplyTeam")
    public Response rejectApplyTeam(@RequestBody TeamRequest request,
                                    HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.REJECT_APPLY_TEAM);
            in.put("remark", request.getRemark());
            in.put("teamApplyLogId", request.getTeamApplyLogId());
            memoMap.put("teamApplyLogId", request.getTeamApplyLogId());
            iTeamBusinessService.rejectApplyTeam(in);
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
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/agreeApplyTeam")
    public Response agreeApplyTeam(@RequestBody TeamRequest request,
                                   HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.AGREE_APPLY_TEAM);
            in.put("remark", request.getRemark());
            in.put("teamApplyLogId", request.getTeamApplyLogId());
            memoMap.put("teamApplyLogId", request.getTeamApplyLogId());
            iTeamBusinessService.agreeApplyTeam(in);
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
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }

    /**
     * 修改我的团队信息
     * 只有管理员可以修改
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/updateMyTeam")
    public Response updateMyTeam(@RequestBody TeamRequest request,
                                 HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.UPDATE_TEAM);
            in.put("teamId", request.getTeamId());
            in.put("name", request.getName());
            in.put("description", request.getDescription());
            iTeamBusinessService.updateMyTeam(in);
            memoMap.put("result", "success");
        } catch (Exception ex) {
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setCode(10001);
                logger.error(ex.getMessage());
            }
            memoMap.put("result", "fail");
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
     * 删除我的团队
     * 只有管理员可以删除
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/deleteMyTeam")
    public Response deleteMyTeam(@RequestBody TeamRequest request,
                                 HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.DELETE_TEAM.toString());
            in.put("teamId", request.getTeamId());
            iTeamBusinessService.deleteMyTeam(in);
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
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }

    /**
     * 统计当前用户未读的团队日志
     * 1、统计未读的加入我的团队申请
     * 2、统计未读的已处理的我加入的团队申请
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/totalMyTeamLogUnread")
    public Response totalNewApplyMember(HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            Map out = iTeamBusinessService.totalMyTeamLogUnread(in);
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
     * 取消我的加入团队申请
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/cancelTeamApplyLog")
    public Response cancelTeamApplyLog(@RequestBody TeamRequest request,
            HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap=new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("teamApplyLogId", request.getTeamApplyLogId());
            logMap.put("token", token);
            logMap.put("actType", GogoActType.CANCEL_TEAM_APPLY);
            logMap.put("memo", "teamApplyLog:"+request.getTeamApplyLogId());
            iTeamBusinessService.cancelTeamApplyLog(in);
            logMap.put("result", true);
        } catch (Exception ex) {
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setCode(10001);
                logger.error(ex.getMessage());
            }
            logMap.put("result", false);
            logMap.put("memo", "teamApplyLog:"+request.getTeamApplyLogId()+"/error:"+ex.getMessage());
        }
        try {
            iCommonBusinessService.createUserActLog(logMap);
        }catch (Exception ex3){
            logger.error(ex3.getMessage());
        }
        return response;
    }

    /**
     * 退出一个团队
     * 创建一个退团申请，等待团队管理员确认
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/quitTeam")
    public Response quitTeam(@RequestBody TeamRequest request,
            HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("teamId", request.getTeamId());
            in.put("remark", request.getRemark());
            logMap.put("token", token);
            logMap.put("actType", GogoActType.QUIT_TEAM);
            memoMap.put("teamId", request.getTeamId());
            iTeamBusinessService.quitTeam(in);
            memoMap.put("result", "success");
        } catch (Exception ex) {
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setCode(10001);
                logger.error(ex.getMessage());
            }
            memoMap.put("result", "fail");
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
     * 查询我发起的退团申请列表
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listTeamQuitLogApply")
    public Response listTeamQuitLogApply(@RequestBody TeamRequest request,
            HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            logMap.put("token", token);
            logMap.put("actType", GogoActType.LIST_TEAM_QUIT_LOG);
            Map out=iTeamBusinessService.listTeamQuitLogApply(in);
            response.setData(out);
            memoMap.put("result", "success");
        } catch (Exception ex) {
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setCode(10001);
                logger.error(ex.getMessage());
            }
            memoMap.put("result", "fail");
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
     * 查询我收到的退团申请列表
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listTeamQuitLogProcess")
    public Response listTeamQuitLogProcess(@RequestBody TeamRequest request,
                                    HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            logMap.put("token", token);
            logMap.put("actType", GogoActType.LIST_TEAM_QUIT_LOG);
            Map out=iTeamBusinessService.listTeamQuitLogProcess(in);
            response.setData(out);
            memoMap.put("result", "success");
        } catch (Exception ex) {
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setCode(10001);
                logger.error(ex.getMessage());
            }
            memoMap.put("result", "fail");
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


}
