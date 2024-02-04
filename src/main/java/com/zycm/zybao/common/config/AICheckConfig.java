package com.zycm.zybao.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 智能内容审核
 * @author: sy
 * @create: 2023-07-13 18:00
 */
@Slf4j
@Configuration
public class AICheckConfig {

    public static String regionId;

    public static String accessKeyId;

    public static String accessKeySecret;

    public static String endPointName;


    @Value("${aicheck.regionId}")
    public void setRegionId(String regionId) {
        AICheckConfig.regionId = regionId;
    }
    @Value("${aliyun.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        AICheckConfig.accessKeyId = accessKeyId;
    }
    @Value("${aliyun.secret}")
    public void setSecret(String accessKeySecret) {
        AICheckConfig.accessKeySecret = accessKeySecret;
    }
    @Value("${aicheck.endPointName}")
    public void setEndPointName(String endPointName) {
        AICheckConfig.endPointName = endPointName;
    }
}
