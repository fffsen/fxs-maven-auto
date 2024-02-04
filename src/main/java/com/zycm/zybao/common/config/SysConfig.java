package com.zycm.zybao.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 系统配置
 * @author: sy
 * @create: 2023-04-23 10:42
 */
@Configuration
public class SysConfig {

    private static String os = System.getProperty("os.name");
    /**
     * 模式支持oss和本地ftp两种模式
     */
    public static String sysFtpModel;

    /**
     * 系统发布模式设置  batch分批发布即延时发布  realtime实时发布无延迟
     */
    public static String sysPublishModel;

    /**
     * 系统版本
     */
    public static String version;

    public static String scode;

    /**
     * 系统素材审核 0人工审核 1预先智能审核
     */
    public static String materialAudit;

    @Value("${sys.ftp.model}")
    public void setSysFtpModel(String sysFtpModel) {
        this.sysFtpModel = sysFtpModel;
    }

    @Value("${sys.publish.model}")
    public void setSysPublishModel(String sysPublishModel) {
        this.sysPublishModel = sysPublishModel;
    }

    @Value("${sys.version}")
    public void setVersion(String version) {
        this.version = version;
    }

    public static String getVersion() {
        if("0".equals(version)){
            version = System.currentTimeMillis()+"";
        }
        return version;
    }

    @Value("${sys.material.audit}")
    public void setMaterialAudit(String materialAudit) {
        this.materialAudit = materialAudit;
    }

    @Value("${sys.scode}")
    public void setScode(String scode) {
        SysConfig.scode = scode;
    }

    public static Boolean systemIsWin(){
        if(os.toLowerCase().startsWith("win")){
            return true;
        }
        return false;
    }
}
