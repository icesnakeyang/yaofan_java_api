package com.gogoyang.yaofan.controller.point;

import lombok.Data;

@Data
public class PointRequest {
    private Double point;
    private String remark;
    private Integer pageIndex;
    private Integer pageSize;
}
