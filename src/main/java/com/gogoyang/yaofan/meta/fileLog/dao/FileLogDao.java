package com.gogoyang.yaofan.meta.fileLog.dao;

import com.gogoyang.yaofan.meta.fileLog.entity.FileLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileLogDao {
    void createFileLog(FileLog fileLog);
}
