package com.zycm.zybao.model.mqmodel.request.screenshot;

/** 远程截屏消息体
* @ClassName: Screenshot
* @Description: TODO
* @author sy
* @date 2017年9月13日 上午10:37:41
*
*/
public class Screenshot {

	private String ipAddr;

	private Integer port;

	private String ftpUser;

	private String ftpPwd;

	private String ftpPath;

	private String shotName;

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
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

	public String getFtpPath() {
		return ftpPath;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}

	public String getShotName() {
		return shotName;
	}

	public void setShotName(String shotName) {
		this.shotName = shotName;
	}




}
