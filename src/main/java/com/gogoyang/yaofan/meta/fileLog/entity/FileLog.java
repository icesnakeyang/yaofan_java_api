package com.gogoyang.yaofan.meta.fileLog.entity;

import lombok.Data;

import java.util.Date;

/**
 * 文件目录里的文件的临时上传日志
 * 业务数据保存时，指定一个fileLogId
 * 如果业务数据表里没有这个fileLogId，则表示该次的上传没有和业务绑定，可以被删除掉
 */
@Data
public class FileLog {
    private Integer ids;
    private String fileLogId;
    private String userId;
    private String filename;
    private Date createTime;
    private String url;
}
