package com.zycm.zybao.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description: ftp相关配置
 * @author: sy
 * @create: 2023-04-23 10:53
 */
@Data
@Configuration
public class FtpConfig {
    /**
     *  ftp的ip
     */
    public static String ftpIp;
    /**
     *  ftp账户
     */
    public static String ftpUsername;
    /**
     *  ftp密码
     */
    public static String ftpPassword;
    /**
     *  ftp端口
     */
    public static int ftpPort;
    /**
     *  ftp编码
     */
    public static String ftpEncode;
    /**
     *  ftp连接模式 被动
     */
    public static String ftpConnectMode;
    /**
     *  ftp服务地址
     */
    public static String ftpServerPath;
    /**
     *  ftp服务md5获取地址
     */
    public static String ftpMasterGetmaterialmd5;
    /**
     *  ftp素材预览地址
     */
    public static String imgServerPath;
    /**
     *  ftp截屏地址格式
     */
    public static String imgServerScreenshotExpression;

    @Value("${ftp.ip}")
    public void setFtpIp(String ftpIp) {
        this.ftpIp = ftpIp;
    }

    @Value("${ftp.username}")
    public void setFtpUsername(String ftpUsername) {
        this.ftpUsername = ftpUsername;
    }
    @Value("${ftp.password}")
    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }
    @Value("${ftp.port}")
    public void setFtpPort(int ftpPort) {
        this.ftpPort = ftpPort;
    }
    @Value("${ftp.encode}")
    public void setFtpEncode(String ftpEncode) {
        this.ftpEncode = ftpEncode;
    }
    @Value("${ftp.connectMode}")
    public void setFtpConnectMode(String ftpConnectMode) {
        this.ftpConnectMode = ftpConnectMode;
    }
    @Value("${ftp.server.path}")
    public void setFtpServerPath(String ftpServerPath) {
        this.ftpServerPath = ftpServerPath;
    }
    @Value("${ftp.master.getmaterialmd5}")
    public void setFtpMasterGetmaterialmd5(String ftpMasterGetmaterialmd5) {
        this.ftpMasterGetmaterialmd5 = ftpMasterGetmaterialmd5;
    }
    @Value("${img.server.path}")
    public void setImgServerPath(String imgServerPath) {
        this.imgServerPath = imgServerPath;
    }

    public static String getImgServerPath() {
        if("oss".equals(SysConfig.sysFtpModel)){
            return OssConfig.preview;
        }
        return imgServerPath;
    }

    @Value("${img.server.screenshot.expression}")
    public void setImgServerScreenshotExpression(String imgServerScreenshotExpression) {
        this.imgServerScreenshotExpression = imgServerScreenshotExpression;
    }
}
