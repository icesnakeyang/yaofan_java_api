package com.gogoyang.yaofan.meta.fileLog.service;


import com.gogoyang.yaofan.meta.fileLog.entity.FileLog;

public interface IFileLogService {
    void createFileLog(FileLog fileLog) throws Exception;
}
