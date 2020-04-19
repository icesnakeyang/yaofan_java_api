package com.gogoyang.yaofan.controller.volunteer;

import lombok.Data;

import java.util.Date;

@Data
public class VolunteerRequest {
    private String title;
    private String content;
    private Date startTime;
    private String pid;
    private String theDate;
    private String theTime;
}
