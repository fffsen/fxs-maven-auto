package com.zycm.zybao.model.mqmodel.request.clientconf;

import com.zycm.zybao.common.config.OssConfig;

/**
* @ClassName: ClientConf
* @Description:修改终端机的配置时发送给终端机的消息 修改ftp及声音、亮度
* @author sy
* @date 2019年3月25日
*
*/
public class ClientConf {

	    private Integer ftpId;

	    private String ftpIp;

	    private Integer ftpPort;

	    private String ftpUser;

	    private String ftpPwd;

	    private Integer light;

	    private Integer voice;
	    //oss使用字段
	    private Integer ftpType;//存储类型 1 ftp服务 2 阿里云存储

	    private String bucketName;//根目录下的主目录

	    private String ossStsServer = OssConfig.ossStsServer;//OSS鉴权服务接口

	    private Integer openHttp = 0;

	    private String httpUrlPrefix;//使用http方式下载的url前端

	    private String ossUploadUrlPrefix;

		public Integer getFtpId() {
			return ftpId;
		}

		public void setFtpId(Integer ftpId) {
			this.ftpId = ftpId;
		}

		public String getFtpUser() {
			return ftpUser;
		}

		public void setFtpUser(String ftpUser) {
			this.ftpUser = ftpUser;
		}

		public String getFtpPwd() {
			return ftpPwd;
		}

		public void setFtpPwd(String ftpPwd) {
			this.ftpPwd = ftpPwd;
		}

		public Integer getLight() {
			return light;
		}

		public void setLight(Integer light) {
			this.light = light;
		}

		public Integer getVoice() {
			return voice;
		}

		public void setVoice(Integer voice) {
			this.voice = voice;
		}

		public String getFtpIp() {
			return ftpIp;
		}

		public void setFtpIp(String ftpIp) {
			this.ftpIp = ftpIp;
		}

		public Integer getFtpPort() {
			return ftpPort;
		}

		public void setFtpPort(Integer ftpPort) {
			this.ftpPort = ftpPort;
		}

		public Integer getFtpType() {
			return ftpType;
		}

		public void setFtpType(Integer ftpType) {
			this.ftpType = ftpType;
		}

		public String getBucketName() {
			return bucketName;
		}

		public void setBucketName(String bucketName) {
			this.bucketName = bucketName;
		}

		public String getOssStsServer() {
			return ossStsServer;
		}

		public void setOssStsServer(String ossStsServer) {
			this.ossStsServer = ossStsServer;
		}

		public String getHttpUrlPrefix() {
			return httpUrlPrefix;
		}

		public void setHttpUrlPrefix(String httpUrlPrefix) {
			this.httpUrlPrefix = httpUrlPrefix;
		}

		public Integer getOpenHttp() {
			return openHttp;
		}

		public void setOpenHttp(Integer openHttp) {
			this.openHttp = openHttp;
		}

		public String getOssUploadUrlPrefix() {
			return ossUploadUrlPrefix;
		}

		public void setOssUploadUrlPrefix(String ossUploadUrlPrefix) {
			this.ossUploadUrlPrefix = ossUploadUrlPrefix;
		}



}
