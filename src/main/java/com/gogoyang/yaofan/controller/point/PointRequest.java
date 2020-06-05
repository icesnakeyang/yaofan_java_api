package com.gogoyang.yaofan.controller.point;

import lombok.Data;

import java.util.Date;

@Data
public class PointRequest {
    private Double point;
    private String remark;
    private Integer pageIndex;
    private Integer pageSize;
    private Date startDate;
    private Date endDate;
}
