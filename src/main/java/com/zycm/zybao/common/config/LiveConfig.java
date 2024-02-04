package com.zycm.zybao.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 监播配置
 * @author: sy
 * @create: 2023-04-25 18:29
 */
@Data
@Configuration
public class LiveConfig {

    public static String protocol;
    public static String ip;
    public static Integer port;
    public static String appName;
    public static String shareAddr;
    public static Integer httpPort;
    public static String defaultPreviewPic;
    @Value("${live.remotePreview.protocol}")
    public void setProtocol(String protocol) {
        LiveConfig.protocol = protocol;
    }
    @Value("${live.remotePreview.ip}")
    public void setIp(String ip) {
        LiveConfig.ip = ip;
    }
    @Value("${live.remotePreview.port}")
    public void setPort(Integer port) {
        LiveConfig.port = port;
    }
    @Value("${live.remotePreview.appName}")
    public void setAppName(String appName) {
        LiveConfig.appName = appName;
    }
    @Value("${live.remotePreview.shareAddr}")
    public void setShareAddr(String shareAddr) {
        LiveConfig.shareAddr = shareAddr;
    }
    @Value("${live.remotePreview.http.port}")
    public void setHttpPort(Integer httpPort) {
        LiveConfig.httpPort = httpPort;
    }
    @Value("${live.default.preview.pic.addr}")
    public void setDefaultPreviewPic(String defaultPreviewPic) {
        LiveConfig.defaultPreviewPic = defaultPreviewPic;
    }
}
