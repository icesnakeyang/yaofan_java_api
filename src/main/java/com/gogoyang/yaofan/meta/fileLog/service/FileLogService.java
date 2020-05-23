package com.gogoyang.yaofan.meta.fileLog.service;

import com.gogoyang.yaofan.meta.fileLog.dao.FileLogDao;
import com.gogoyang.yaofan.meta.fileLog.entity.FileLog;
import org.springframework.stereotype.Service;

@Service
public class FileLogService implements IFileLogService{
    private final FileLogDao fileLogDao;

    public FileLogService(FileLogDao fileLogDao) {
        this.fileLogDao = fileLogDao;
    }

    @Override
    public void createFileLog(FileLog fileLog) throws Exception {
        fileLogDao.createFileLog(fileLog);
    }
}
