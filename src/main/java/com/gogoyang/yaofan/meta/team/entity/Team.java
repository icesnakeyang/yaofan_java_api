package com.gogoyang.yaofan.meta.team.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Team {
    private Integer ids;
    private String teamId;
    private String name;
    private String description;
    private Date createTime;
    private String userId;
    private String status;
}
