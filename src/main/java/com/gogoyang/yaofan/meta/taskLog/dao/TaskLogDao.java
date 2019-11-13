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

    /**
     * 统计任务的日志总数
     * @param qIn
     * @return
     */
    Integer totalTaskLog(Map qIn);

    /**
     * 统计当前用户未阅读的日志总数
     * @param qIn
     * @return
     */
    Integer totalTaskLogUnread(Map qIn);
}
