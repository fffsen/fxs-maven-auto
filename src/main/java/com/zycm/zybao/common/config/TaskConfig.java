package com.zycm.zybao.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 定时任务设置
 * @author: sy
 * @create: 2023-04-23 16:22
 */
@Configuration
public class TaskConfig {
    /**
     *  统计离线频率的时间差值，单位为分钟
     */

    public static Integer taskNoticeTimedff;
    /**
     *  需延迟推送的频率值
     */

    public static Integer taskNoticeFrequency;
    /**
     *  推送的时间范围 开始
     */

    public static String taskNoticeStarttime;
    /**
     *  推送的时间范围 结束
     */

    public static String taskNoticeEtime;

    @Value("${task.notice.timedff}")
    public void setTaskNoticeTimedff(Integer taskNoticeTimedff) {
        this.taskNoticeTimedff = taskNoticeTimedff;
    }
    @Value("${task.notice.frequency}")
    public void setTaskNoticeFrequency(Integer taskNoticeFrequency) {
        this.taskNoticeFrequency = taskNoticeFrequency;
    }
    @Value("${task.notice.starttime}")
    public void setTaskNoticeStarttime(String taskNoticeStarttime) {
        this.taskNoticeStarttime = taskNoticeStarttime;
    }
    @Value("${task.notice.etime}")
    public void setTaskNoticeEtime(String taskNoticeEtime) {
        this.taskNoticeEtime = taskNoticeEtime;
    }
}
