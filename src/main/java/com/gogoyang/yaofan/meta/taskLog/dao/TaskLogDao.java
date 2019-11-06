package com.gogoyang.yaofan.meta.taskLog.dao;

import com.gogoyang.yaofan.meta.taskLog.entity.TaskLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskLogDao {
    void createTaskLog(TaskLog taskLog);
}
