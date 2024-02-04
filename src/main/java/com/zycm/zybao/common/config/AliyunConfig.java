package com.zycm.zybao.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: sy
 */
@Slf4j
@Configuration
public class AliyunConfig {

    public static String regionId;

    public static String accessKeyId;

    public static String secret;

    public static String roleArn;

    public static String roleSessionName;

    @Value("${aliyun.regionId}")
    public void setRegionId(String regionId) {
        AliyunConfig.regionId = regionId;
    }
    @Value("${aliyun.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        AliyunConfig.accessKeyId = accessKeyId;
    }
    @Value("${aliyun.secret}")
    public void setSecret(String secret) {
        AliyunConfig.secret = secret;
    }
    @Value("${aliyun.roleArn}")
    public void setRoleArn(String roleArn) {
        AliyunConfig.roleArn = roleArn;
    }
    @Value("${aliyun.roleSessionName}")
    public void setRoleSessionName(String roleSessionName) {
        AliyunConfig.roleSessionName = roleSessionName;
    }
}
