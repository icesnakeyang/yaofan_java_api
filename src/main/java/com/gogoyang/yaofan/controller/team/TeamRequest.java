package com.gogoyang.yaofan.controller.team;

import lombok.Data;

@Data
public class TeamRequest {
    private String name;
    private String description;
    private String teamId;
    private String remark;
    /**
     * 申请加入团队的id
     */
    private String applyId;

    private Integer pageIndex;
    private Integer pageSize;

    private String teamApplyLogId;
}
