package com.gogoyang.yaofan.meta.taskLog.dao;

import com.gogoyang.yaofan.meta.taskLog.entity.TaskLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface TaskLogDao {
    void createTaskLog(TaskLog taskLog);

    ArrayList<TaskLog> listTaskLog(Map qIn);

    void setTaskLogReadTime(Map qIn);
}
