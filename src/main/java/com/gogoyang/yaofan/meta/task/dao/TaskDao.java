package com.gogoyang.yaofan.meta.task.dao;

import com.gogoyang.yaofan.meta.task.entity.Task;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskDao {

    void createTask(Task task);
}
