package com.zycm.zybao.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 微信公众号配置
 * @author: sy
 * @create: 2023-04-23 15:21
 */
@Data
@Configuration
public class WxConfig {
    /**
     *  公众号mpId
     */

    public static String mpId;
    /**
     *  公众号appid
     */

    public static String appId;
    /**
     *  公众号appSecret
     */

    public static String appSecret;
    /**
     *  公众号token
     */

    public static String token;
    /**
     *  公众号templateId
     */

    public static String templateId;
    /**
     *  公众号回调url
     */

    public static String url;

    @Value("${wx.warn.notice.mpId}")
    public void setMpId(String mpId) {
        this.mpId = mpId;
    }
    @Value("${wx.warn.notice.appId}")
    public void setAppId(String appId) {
        this.appId = appId;
    }
    @Value("${wx.warn.notice.appSecret}")
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
    @Value("${wx.warn.notice.token}")
    public void setToken(String token) {
        this.token = token;
    }
    @Value("${wx.warn.notice.templateId}")
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    @Value("${wx.warn.notice.url}")
    public void setUrl(String url) {
        this.url = url;
    }
}
