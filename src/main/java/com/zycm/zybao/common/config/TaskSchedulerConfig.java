package com.zycm.zybao.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @description: 定时任务配置
 * @author: sy
 * @create: 2023-04-27 14:21
 */

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "task.enabled",matchIfMissing = true)
public class TaskSchedulerConfig {

    private Integer poolSize;

    @Bean
    public TaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(poolSize);
        threadPoolTaskScheduler.setThreadNamePrefix("ZYCM-TaskScheduler");
        threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);
        return threadPoolTaskScheduler;
    }

    @Value("${task.poolSize}")
    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }
}
