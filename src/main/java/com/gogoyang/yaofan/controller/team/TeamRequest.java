package com.gogoyang.yaofan.controller.team;

import lombok.Data;

@Data
public class TeamRequest {
    private String name;
    private String description;
    private String teamId;
    private String remark;
    private Integer pageIndex;
    private Integer pageSize;

    private String teamApplyLogId;
    private String teamQuitLogId;
}
