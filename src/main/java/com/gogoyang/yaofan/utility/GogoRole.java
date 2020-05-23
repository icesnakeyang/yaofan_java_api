package com.gogoyang.yaofan.utility;

public enum GogoRole {
    SUPER_ADMIN,
    ADMIN,
    SECRETARY,
    /**
     * 团队任务观察员
     * 团队管理员可以设置团队成员为观察员
     * 观察员可以查看所有的团队成员的所有任务，但只能查看，不能进行任务操作
     */
    TEAM_OBSERVER
}
