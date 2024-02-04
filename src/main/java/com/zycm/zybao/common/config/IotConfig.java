package com.zycm.zybao.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 物联网配置
 * @author: sy
 * @create: 2023-04-23 13:58
 */
@Data
@Configuration
public class IotConfig {

    /**
     *  电信物联卡 api地址前缀
     */
    public static String telecomPrefix;
    /**
     *  电信物联卡 用户id
     */

    public static String telecomUserId;
    /**
     *  电信物联卡 用户密码
     */

    public static String telecomPassWord;
    /**
     *  电信物联卡 用户key
     */

    public static String telecomKey;




    /**
     *  联通物联卡 api地址前缀
     */

    public static String unicomPrefix;
    /**
     *  联通物联卡 用户1
     */

    public static String unicomUsername;
    /**
     *  联通物联卡 用户1密码
     */

    public static String unicomSecretkey;
    /**
     *  联通物联卡 用户2
     */

    public static String unicomUsername2;
    /**
     *  联通物联卡 用户2密码
     */

    public static String unicomSecretkey2;
    /**
     *  联通物联卡 卡号查询api
     */

    public static String unicomCtdUsages;






    /**
     *  移动物联卡 api地址前缀
     */

    public static String mobilePrefix;
    /**
     *  移动物联卡 api地址前缀
     */

    public static String mobileUsername;
    /**
     *  移动物联卡 api地址前缀
     */

    public static String mobileKey;

    @Value("${telecom.iot.https.prefix}")
    public void setTelecomPrefix(String telecomPrefix) {
        this.telecomPrefix = telecomPrefix;
    }
    @Value("${telecom.iot.user_id}")
    public void setTelecomUserId(String telecomUserId) {
        this.telecomUserId = telecomUserId;
    }
    @Value("${telecom.iot.passWord}")
    public void setTelecomPassWord(String telecomPassWord) {
        this.telecomPassWord = telecomPassWord;
    }
    @Value("${telecom.iot.key}")
    public void setTelecomKey(String telecomKey) {
        this.telecomKey = telecomKey;
    }
    @Value("${unicom.iot.https.prefix}")
    public void setUnicomPrefix(String unicomPrefix) {
        this.unicomPrefix = unicomPrefix;
    }
    @Value("${unicom.iot.username}")
    public void setUnicomUsername(String unicomUsername) {
        this.unicomUsername = unicomUsername;
    }
    @Value("${unicom.iot.secretkey}")
    public void setUnicomSecretkey(String unicomSecretkey) {
        this.unicomSecretkey = unicomSecretkey;
    }
    @Value("${unicom.iot.username2}")
    public void setUnicomUsername2(String unicomUsername2) {
        this.unicomUsername2 = unicomUsername2;
    }
    @Value("${unicom.iot.secretkey2}")
    public void setUnicomSecretkey2(String unicomSecretkey2) {
        this.unicomSecretkey2 = unicomSecretkey2;
    }
    @Value("${unicom.iot.url.ctdUsages}")
    public void setUnicomCtdUsages(String unicomCtdUsages) {
        this.unicomCtdUsages = unicomCtdUsages;
    }
    @Value("${mobile.iot.https.prefix}")
    public void setMobilePrefix(String mobilePrefix) {
        this.mobilePrefix = mobilePrefix;
    }
    @Value("${mobile.iot.username}")
    public void setMobileUsername(String mobileUsername) {
        this.mobileUsername = mobileUsername;
    }
    @Value("${mobile.iot.key}")
    public void setMobileKey(String mobileKey) {
        this.mobileKey = mobileKey;
    }
}
