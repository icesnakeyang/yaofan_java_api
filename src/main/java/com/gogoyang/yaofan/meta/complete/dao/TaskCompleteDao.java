package com.gogoyang.yaofan.meta.complete.dao;

import com.gogoyang.yaofan.meta.complete.entity.TaskComplete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskCompleteDao {
    void createTaskComplete(TaskComplete taskComplete);
}
