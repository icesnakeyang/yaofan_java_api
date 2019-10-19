package com.gogoyang.yaofan.business.team;

import com.gogoyang.yaofan.meta.team.entity.Team;
import com.gogoyang.yaofan.meta.team.service.ITeamService;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.GogoTools;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class TeamBusinessService implements ITeamBusinessService {
    private final ICommonBusinessService iCommonBusinessService;
    private final ITeamService iTeamService;

    public TeamBusinessService(ICommonBusinessService iCommonBusinessService,
                               ITeamService iTeamService) {
        this.iCommonBusinessService = iCommonBusinessService;
        this.iTeamService = iTeamService;
    }

    @Override
    public Map createTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        String name = in.get("name").toString();
        String description = in.get("description").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Team team = new Team();
        team.setCreateTime(new Date());
        team.setDescription(description);
        team.setName(name);
        team.setStatus(GogoStatus.ACTIVE.toString());
        team.setTeamId(GogoTools.UUID().toString());
        team.setUserId(userInfo.getUserId());
        team = iTeamService.createTeam(team);

        return ;
    }
}
