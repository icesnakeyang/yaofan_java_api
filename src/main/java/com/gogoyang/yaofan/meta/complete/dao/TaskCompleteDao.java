package com.gogoyang.yaofan.meta.complete.dao;

import com.gogoyang.yaofan.meta.complete.entity.TaskComplete;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface TaskCompleteDao {
    void createTaskComplete(TaskComplete taskComplete);

    /**
     * 读取完成日志列表
     * @param qIn
     * taskId
     * @return
     */
    ArrayList<TaskComplete> listTaskComplete(Map qIn);

    Integer totalCompleteUnread(Map qIn);

    void setTaskCompleteReadTime(Map qIn);

    Integer totalTaskComplete(Map qIn);
}
