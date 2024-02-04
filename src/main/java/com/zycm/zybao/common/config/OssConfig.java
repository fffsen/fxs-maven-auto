package com.zycm.zybao.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description: oss配置
 * @author: sy
 * @create: 2023-04-23 11:16
 */
@Data
@Configuration
public class OssConfig {

    public static String endpoint;

    public static String accessKeyId;

    public static String accessKeySecret;

    public static String bucketName;

    public static String preview;

    public static String ossStsServer;//OSS鉴权服务接口

    @Value("${oss.endpoint}")
    public void setEndpoint(String endpoint) {
        OssConfig.endpoint = endpoint;
    }
    @Value("${oss.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        OssConfig.accessKeyId = accessKeyId;
    }
    @Value("${oss.accessKeySecret}")
    public void setAccessKeySecret(String accessKeySecret) {
        OssConfig.accessKeySecret = accessKeySecret;
    }
    @Value("${oss.bucketName}")
    public void setBucketName(String bucketName) {
        OssConfig.bucketName = bucketName;
    }
    @Value("${oss.preview}")
    public void setPreview(String preview) {
        OssConfig.preview = preview;
    }
    @Value("${oss.ossStsServer}")
    public void setOssStsServer(String ossStsServer) {
        OssConfig.ossStsServer = ossStsServer;
    }
}
